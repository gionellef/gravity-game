package com.megahard.gravity.util;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.megahard.gravity.engine.GameMap;

public class Pathfinder {

	public static class Grid {
		public int width;
		public int height;
		public Node[] grid;

		public Grid(int w, int h) {
			width = w;
			height = h;
			grid = new Node[w * h];

			int x = 0;
			int y = 0;
			for (int i = 0; i < grid.length; i++) {
				Node node = new Node(x, y);
				grid[i] = node;
				if (y > 0) {
					if (x > 0) {
						(node.neighbors[0] = grid[i - 1 - w]).neighbors[7] = node;
					}
					(node.neighbors[1] = grid[i - w]).neighbors[6] = node;
					if (x < w - 1) {
						(node.neighbors[2] = grid[i + 1 - w]).neighbors[5] = node;
					}
				}
				if (x > 0) {
					(node.neighbors[3] = grid[i - 1]).neighbors[4] = node;
				}

				x++;
				if (x >= w) {
					x = 0;
					y++;
				}
			}
		}

		public Node getNode(int x, int y) {
			return grid[y * width + x];
		}

		public void clear() {
			for (int i = 0; i < grid.length; i++) {
				Node n = grid[i];
				n.parent = null;
				n.f = n.g = n.h = 0;
				n.opened = false;
				n.closed = false;
			}
		}
	}

	public static class Node implements Comparable<Node> {
		public int x;
		public int y;
		public Node parent = null;

		public double f = 0;
		public double g = 0;
		public double h = 0;

		public boolean opened = false;
		public boolean closed = false;

		public Node[] neighbors = new Node[8];

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Node o) {
			return f == o.f ? 0 : f < o.f ? -1 : 1;
		}
	}

	private GameMap map;
	private Vector2 size;

	private Grid grid;

	public Pathfinder(GameMap map, Vector2 size) {
		this.map = map;
		this.size = size;
		grid = new Grid(map.getWidth(), map.getHeight());
	}

	public List<Vector2> findPath(Vector2 start, Vector2 end) {
		grid.clear();
		start = start.plus(0.5, 0.5);
		if (start.x < 0 || start.y < 0 || start.x >= map.getWidth()
				|| start.y >= map.getHeight()) {
			return null;
		}
		if (end.x < 0 || end.y < 0 || end.x >= map.getWidth()
				|| end.y >= map.getHeight()) {
			return null;
		}
		if (!passable(end.x, end.y)) {
			return null;
		}
		Node endNode = grid.getNode((int) end.x, (int) end.y);
		Node startNode = grid.getNode((int) start.x, (int) start.y);

		PriorityQueue<Node> open = new PriorityQueue<>();
		startNode.f = 0;
		startNode.closed = true;
		open.add(startNode);

		boolean found = false;

		do {
			Node current = open.remove();
			current.closed = true;

			if (current == endNode) {
				found = true;
				break;
			}

			for (Node n : successors(current, endNode)) {
				if (n == null)
					continue;

				double d = Math.abs(n.x - current.x)
						+ Math.abs(n.y - current.y);
				double g = current.g + d;

				if (!n.opened || g < n.g) {
					n.g = g;
					n.h = end.distance(n.x, n.y);
					n.f = n.g + n.h;

					n.parent = current;

					if (n.opened) {
						open.remove(n);
					} else {
						n.opened = true;
					}
					open.add(n);
				}
			}
		} while (!open.isEmpty());

		if (found) {
			List<Vector2> points = new LinkedList<>();
			points.add(end);
			Node n = endNode.parent;
			while (n != null) {
				Vector2 vec = new Vector2(n.x, n.y);
				points.add(0, vec);
				n = n.parent;
			}
			return points;
		} else {
			return null;
		}
	}

	private Node[] successors(Node node, Node endNode) {
		Node[] succ = new Node[8];
		int c = 0;
		Node[] neighbors = findNeighbors(node);
		for (Node n : neighbors) {
			if (n == null)
				continue;

			Node j = jump(node, n, endNode);
			if (j == null)
				continue;
			if (j.closed)
				continue;

			succ[c++] = j;
		}
		return succ;
	}

	private Node jump(Node from, Node node, Node endNode) {
		if (node == endNode) {
			return node;
		}
		if (!passable(node)) {
			return null;
		}
		int dx = (node.x - from.x) / Math.max(Math.abs(node.x - from.x), 1);
		int dy = (node.y - from.y) / Math.max(Math.abs(node.y - from.y), 1);

		Node nnx = node.neighbors[3];
		Node npx = node.neighbors[4];
		Node nny = node.neighbors[1];
		Node npy = node.neighbors[6];
		Node nvxny = node.neighbors[dx > 0 ? 0 : 2];
		Node nvxpy = node.neighbors[dx > 0 ? 5 : 7];
		Node nnxvy = node.neighbors[dy > 0 ? 0 : 5];
		Node npxvy = node.neighbors[dy > 0 ? 2 : 7];
		Node nx = dx < 0 ? nnx : npx;
		Node ny = dy < 0 ? nny : npy;
		Node nxy = node.neighbors[(dx < 0 ? 0 : 2) + (dy < 0 ? 0 : 5)];

		if (dx != 0 && dy != 0) {
			if (jump(node, nx, endNode) != null
					|| jump(node, ny, endNode) != null) {
				return node;
			}
			if (passable(nx) && passable(ny)) {
				return jump(node, nxy, endNode);
			}
		} else {
			if (dx != 0) {
				if ((passable(npy) && !passable(nvxpy))
						|| (passable(nny) && !passable(nvxny))) {
					return node;
				}
				if (passable(nx)) {
					return jump(node, nx, endNode);
				}
			} else if (dy != 0) {
				if ((passable(npx) && !passable(npxvy))
						|| (passable(nnx) && !passable(nnxvy))) {
					return node;
				}
				if (passable(ny)) {
					return jump(node, ny, endNode);
				}
			}
		}

		return null;
	}

	private Node[] findNeighbors(Node node) {
		Node parent = node.parent;
		if (parent == null) {
			Node[] neighbors = new Node[8];
			for (int i = 0; i < node.neighbors.length; i++) {
				Node n = node.neighbors[i];
				if (passable(n)) {
					neighbors[i] = n;
				}
			}
			return neighbors;
		}

		Node[] neighbors = new Node[5];
		int dx = (node.x - parent.x) / Math.max(Math.abs(node.x - parent.x), 1);
		int dy = (node.y - parent.y) / Math.max(Math.abs(node.y - parent.y), 1);

		Node nnx = node.neighbors[3];
		Node npx = node.neighbors[4];
		Node nny = node.neighbors[1];
		Node npy = node.neighbors[6];
		Node nx = dx < 0 ? nnx : npx;
		Node ny = dy < 0 ? nny : npy;
		Node nxy = node.neighbors[(dx < 0 ? 0 : 2) + (dy < 0 ? 0 : 5)];

		if (dx != 0 && dy != 0) {
			if (passable(nx)) {
				neighbors[0] = nx;
			}
			if (passable(ny)) {
				neighbors[1] = ny;
			}
			if (passable(nx) && passable(ny)) {
				neighbors[2] = nxy;
			}
		} else {
			Node nnxy = node.neighbors[dy < 0 ? 0 : 5];
			Node npxy = node.neighbors[dy < 0 ? 2 : 7];
			Node nxny = node.neighbors[dx < 0 ? 0 : 2];
			Node nxpy = node.neighbors[dx < 0 ? 5 : 7];

			if (dx != 0) {
				boolean passny = passable(nny);
				boolean passpy = passable(npy);
				if (passable(nx)) {
					neighbors[0] = nx;
					if (passny) {
						neighbors[1] = nxny;
					}
					if (passpy) {
						neighbors[2] = nxpy;
					}
				}
				if (passny) {
					neighbors[3] = nny;
				}
				if (passpy) {
					neighbors[4] = npy;
				}
			} else if (dy != 0) {
				boolean passnx = passable(nnx);
				boolean passpx = passable(npx);
				if (passable(ny)) {
					neighbors[0] = ny;
					if (passnx) {
						neighbors[1] = nnxy;
					}
					if (passpx) {
						neighbors[2] = npxy;
					}
				}
				if (passnx) {
					neighbors[3] = nnx;
				}
				if (passpx) {
					neighbors[4] = npx;
				}
			}
		}
		return neighbors;
	}

	private boolean passable(Node n) {
		if (n == null)
			return false;

		return passable(n.x, n.y);
	}

	public boolean passable(double x, double y) {
		int xmin = (int) (x - size.x / 2);
		int xmax = (int) (x + size.x / 2);
		int ymin = (int) (y - size.y / 2);
		int ymax = (int) (y + size.y / 2);
		for (int i = (int) xmin; i <= xmax; i++) {
			for (int j = (int) ymin; j <= ymax; j++) {
				if (map.getTile(i, j).getCollidable())
					return false;
			}
		}
		return true;
	}
}
