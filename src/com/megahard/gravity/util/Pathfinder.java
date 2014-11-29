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
//						(node.neighbors[0] = grid[i - 1 - w]).neighbors[7] = node;
					}
					(node.neighbors[1] = grid[i - w]).neighbors[6] = node;
					if(x < w - 1){
//						(node.neighbors[2] = grid[i + 1 - w]).neighbors[5] = node;
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
		
		public void clear(){
			for(int i = 0; i < grid.length; i++){
				Node n = grid[i];
				n.parent = null;
				n.cost = 0;
				n.closed = false;
			}
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
	private Vector2 size;
	
	private Grid grid;
	
	public Pathfinder(GameMap map, Vector2 size) {
		this.map = map;
		this.size = size;
		grid = new Grid(map.getWidth(), map.getHeight());
	}

	public List<Vector2> findPath(Vector2 start, Vector2 end){
		grid.clear();
		start = start.plus(0.5, 0.5);
		end = end.plus(0.5, 0.5);
		if(start.x < 0 || start.y < 0 || start.x >= map.getWidth() || start.y >= map.getHeight()){
			return null;
		}
		if(end.x < 0 || end.y < 0 || end.x >= map.getWidth() || end.y >= map.getHeight()){
			return null;
		}
		Node endNode = grid.getNode((int)end.x, (int)end.y);
		Node startNode = grid.getNode((int)start.x, (int)start.y);

		PriorityQueue<Node> open = new PriorityQueue<>();
		startNode.cost = 0;
		startNode.closed = true;
		open.add(startNode);
		
		boolean found = false;
		
		while(!open.isEmpty()){
			Node current = open.remove();
			
			if(current == endNode){
				found = true;
				break;
			}
			
			for(Node n : current.neighbors){
				if(n == null) continue;
				if(n.closed) continue;
				n.closed = true;
				if(!passable(n)) continue;
				
				double d = end.distance(n.x, n.y);
				
				n.cost = current.cost + 2 + d;
				n.parent = current;
				open.add(n);
			}
		}

		if(found){
			List<Vector2> points = new LinkedList<>();
			points.add(end);
			Node n = endNode.parent;
			while(n != null){
				Vector2 vec = new Vector2(n.x, n.y);
				points.add(0, vec);
				n = n.parent;
			}
			return points;
		}else{
			return null;
		}
	}

	private boolean passable(Node n) {
		double xmin = n.x - size.x/2;
		double xmax = n.x + size.x/2;
		double ymin = n.y - size.y/2;
		double ymax = n.y + size.y/2;
		for(double i = xmin; i <= xmax; i++){
			for(double j = ymin; j <= ymax; j++){
				if(map.getTile(i, j).getCollidable()) return false;		
			}	
		}
		return true;
	}
}
