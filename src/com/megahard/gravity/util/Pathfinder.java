package com.megahard.gravity.util;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.megahard.gravity.engine.GameMap;

public class Pathfinder {

	public static class Grid{
		public int width;
		public int height;
		public Node[] grid;
		
		public Grid(int w, int h){
			width = w;
			height = h;
			grid = new Node[w * h];
			
			int x = 0;
			int y = 0;
			for(int i = 0; i < grid.length; i++){
				Node node = new Node(x, y);
				grid[i] = node;
				if(y > 0){
					if(x > 0){
						(node.neighbors[0] = grid[i - 1 - w]).neighbors[7] = node;
					}
					(node.neighbors[1] = grid[i - w]).neighbors[6] = node;
					if(x < w - 1){
						(node.neighbors[2] = grid[i + 1 - w]).neighbors[5] = node;
					}
				}
				if(x > 0){
					(node.neighbors[3] = grid[i - 1]).neighbors[4] = node;
				}
				
				x++;
				if(x >= w){
					x = 0;
					y++;
				}
			}
		}
		
		public Node getNode(int x, int y){
			return grid[y * width + x];
		}
	}
	
	public static class Node implements Comparable<Node>{
		public int x;
		public int y;
		public Node parent = null;
		public double cost = 0;
		public boolean closed = false;
		
		public Node[] neighbors = new Node[8];

		public Node(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int compareTo(Node o) {
			return cost == o.cost ? 0 : cost < o.cost ? -1 : 1;
		}
	}
	
	private GameMap map;
	
	public Pathfinder(GameMap map) {
		this.map = map;
	}

	public List<Vector2> findPath(Vector2 start, Vector2 end, double radius){
		Grid grid = new Grid(map.getWidth(), map.getHeight());
		
		Node endNode = grid.getNode((int)end.x, (int)end.y);
		Node startNode = grid.getNode((int)start.x, (int)start.y);

		PriorityQueue<Node> open = new PriorityQueue<>();
		startNode.cost = 0;
		open.add(startNode);
		
		boolean found = false;
		
		while(!open.isEmpty()){
			Node current = open.remove();
			current.closed = true;
			
			if(current == endNode){
				found = true;
				break;
			}
			
			for(Node n : current.neighbors){
				if(n == null) continue;
				if(n.closed) continue;
				if(!passable(n, radius)) continue;
				
				n.cost = current.cost + 1;
				n.parent = current;
				open.add(n);
			}
		}

		if(found){
			List<Vector2> points = new LinkedList<>();
			Node n = endNode;
			do{
				points.add(0, new Vector2(n.x + 0.5, n.y + 0.5));
				n = n.parent;
			}while(n != null);
			return points;
		}else{
			return null;
		}
	}

	private boolean passable(Node n, double radius) {
		int r = (int) Math.ceil(radius);
		for(int i=-r; i<=r; i++){
			for(int j=-r; j<=r; j++){
				if(map.getTile(n.x + i, n.y + j).getCollidable()) return false;		
			}	
		}
		return true;
	}
	
}
