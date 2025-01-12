import javax.swing.*;
import javax.swing.tree.*;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class LifeTree extends TreeFrame {

    static Scanner sc;
	private ArrayList<String> lines;
	MyDefaultMutableTreeNode root;

    // Overrides method in TreeFrame
	@Override
    void initTree() {
		this.lines = new ArrayList<>();
		buildTree();
		this.treeModel = new DefaultTreeModel( this.root );
		this.tree = new JTree( this.treeModel );
		this.tree.setBackground(Color.GREEN);

    }

    void buildTree() {
		for ( ; sc.hasNextLine() ; ) {
			this.lines.add(sc.nextLine());
		}
		this.root = this.buildTree(0, this.lines.size()-1);

	} 

	MyDefaultMutableTreeNode buildTree(int start, int end) {
		String name = MyDefaultMutableTreeNode.lineToName(this.lines.get(start));
		String description = MyDefaultMutableTreeNode.lineToDescription(this.lines.get(start));
		String level = MyDefaultMutableTreeNode.lineToLevel(this.lines.get(start));

		MyDefaultMutableTreeNode node = new MyDefaultMutableTreeNode(level, name, description);

		int indexStart;
		int indexEnd = start;
		String currentLevel;
		Boolean endtagExists = true;
		while (indexEnd != end-1 && endtagExists) {
			indexStart = indexEnd + 1;
			currentLevel = this.lines.get(indexStart).split(">")[0].replaceFirst("<", "").split(" ")[0];
			for (int i=indexStart + 1; ; i++) {
				try {
					if (this.lines.get(i).strip().equals("</" + currentLevel + ">")) {
						indexEnd = i;
						node.add(this.buildTree(indexStart, indexEnd));
						break;
					} 
				} catch (Exception e) {
					System.err.println("Endtag " + currentLevel + " is missing");
					System.exit(0);
				}
				/* 
				else if (i == end) {
					try {
						throw new Exception("Endtag is missing");
					} catch (Exception e){
						System.err
					}
					//System.out.println("Endtag " + currentLevel + " is missing");
					//endtagExists = false;
					//System.err.println("Endtag " + currentLevel + " is missing");
					//break;
					
				}
				*/
			}
		}

		return node;
	}

    // Overrides method in TreeFrame
    void showDetails(TreePath path){
		if (path == null)
			return;
		MyDefaultMutableTreeNode clicked = (MyDefaultMutableTreeNode) path.getLastPathComponent();
		String s = clicked.getRank() + ": " + clicked.toString() + " " + clicked.getDescription() + ", Men allt som är " + clicked.toString();
		MyDefaultMutableTreeNode parent = clicked;
		while (parent.getParent() != null) {
			System.out.println(parent.toString());
			parent = (MyDefaultMutableTreeNode) parent.getParent();
			s = s + " är " + parent.toString();
		}
		JOptionPane.showMessageDialog(this, s);
    }

    public static void main(String[] args) {
		String file;
		if (args.length>0) {
			file = args[0];
		} else {
			file = "LillaLiv.txt";
		} 
		try {
			LifeTree.sc = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		new LifeTree();
    }

}
