package com.sg.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.sg.main.Constants;

// leaf node icon을 설정하기 위한 클래스
class MyTreeRenderer extends DefaultTreeCellRenderer {
	Icon[] iconPack;

	//각 확장자에 사용할 아이콘들 받아오기
	public MyTreeRenderer() {
		iconPack = new Icon[20];
		iconPack[0] = new ImageIcon(this.getClass().getResource(Constants.IconPath.forderIcon.getPath()));
		iconPack[1] = new ImageIcon(this.getClass().getResource(Constants.IconPath.fileIcon.getPath()));
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded,boolean leaf,int row,boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		String str = value.toString();
		if(leaf) {
			if(str.indexOf('.')==-1)
				// 확장자가 없으면 forderIcon
				setIcon(iconPack[0]);
			else {
				// 확장자가 있으면 fileIcon
				setIcon(iconPack[1]);
			}
		} 
		return this;
	}
}