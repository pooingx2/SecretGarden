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
		iconPack = new Icon[30];
		iconPack[0] = new ImageIcon(Constants.IconPath.forderIcon.getPath());
		iconPack[1] = new ImageIcon(Constants.IconPath.fileIcon.getPath());
		iconPack[2] = new ImageIcon(Constants.IconPath.aviFileIcon.getPath());
		iconPack[3] = new ImageIcon(Constants.IconPath.bmpFileIcon.getPath());
		iconPack[4] = new ImageIcon(Constants.IconPath.xlsxFileIcon.getPath());
		iconPack[5] = new ImageIcon(Constants.IconPath.exeFileIcon.getPath());
		iconPack[6] = new ImageIcon(Constants.IconPath.flashFileIcon.getPath());
		iconPack[7] = new ImageIcon(Constants.IconPath.gifFileIcon.getPath());
		iconPack[8] = new ImageIcon(Constants.IconPath.jpgFileIcon.getPath());
		iconPack[9] = new ImageIcon(Constants.IconPath.mp3FileIcon.getPath());
		iconPack[10] = new ImageIcon(Constants.IconPath.pdfFileIcon.getPath());
		iconPack[11] = new ImageIcon(Constants.IconPath.pngFileIcon.getPath());
		iconPack[12] = new ImageIcon(Constants.IconPath.pptFileIcon.getPath());
		iconPack[13] = new ImageIcon(Constants.IconPath.smiFileIcon.getPath());
		iconPack[14] = new ImageIcon(Constants.IconPath.txtFileIcon.getPath());
		iconPack[15] = new ImageIcon(Constants.IconPath.docFileIcon.getPath());
		iconPack[16] = new ImageIcon(Constants.IconPath.zipFileIcon.getPath());
		iconPack[17] = new ImageIcon(Constants.IconPath.psdFileIcon.getPath());
		iconPack[18] = new ImageIcon(Constants.IconPath.xmlFileIcon.getPath());
		iconPack[19] = new ImageIcon(Constants.IconPath.jarFileIcon.getPath());
		iconPack[20] = new ImageIcon(Constants.IconPath.gulFileIcon.getPath());
		iconPack[21] = new ImageIcon(Constants.IconPath.hwpFileIcon.getPath());
		iconPack[22] = new ImageIcon(Constants.IconPath.htmlFileIcon.getPath());
		iconPack[23] = new ImageIcon(Constants.IconPath.mediaFileIcon.getPath());
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
				
				// 이미지 파일
				if(str.substring(str.length()-4, str.length()).contains("jpg")) setIcon(iconPack[8]);
				else if(str.substring(str.length()-4, str.length()).contains("JPG")) setIcon(iconPack[8]);
				else if(str.substring(str.length()-4, str.length()).contains("jpeg")) setIcon(iconPack[8]);
				else if(str.substring(str.length()-4, str.length()).contains("bmp")) setIcon(iconPack[3]);
				else if(str.substring(str.length()-4, str.length()).contains("gif")) setIcon(iconPack[7]);
				else if(str.substring(str.length()-4, str.length()).contains("png")) setIcon(iconPack[11]);
				else if(str.substring(str.length()-4, str.length()).contains("psd")) setIcon(iconPack[17]);
				else if(str.substring(str.length()-4, str.length()).contains("swc")) setIcon(iconPack[6]);
				// 동영상 파일
				else if(str.substring(str.length()-4, str.length()).contains("avi")) setIcon(iconPack[2]);
				else if(str.substring(str.length()-4, str.length()).contains("mpeg")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("asf")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("mp4")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("mov")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("wmv")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("swf")) setIcon(iconPack[23]);
				else if(str.substring(str.length()-4, str.length()).contains("smi")) setIcon(iconPack[13]);
				// 오디오 파일
				else if(str.substring(str.length()-4, str.length()).contains("mp3")) setIcon(iconPack[9]);
				else if(str.substring(str.length()-4, str.length()).contains("wma")) setIcon(iconPack[9]);
				else if(str.substring(str.length()-4, str.length()).contains("wav")) setIcon(iconPack[9]);
				// 문서파일
				else if(str.substring(str.length()-4, str.length()).contains("txt")) setIcon(iconPack[14]);
				else if(str.substring(str.length()-4, str.length()).contains("hwp")) setIcon(iconPack[21]);
				else if(str.substring(str.length()-4, str.length()).contains("doc")) setIcon(iconPack[15]);
				else if(str.substring(str.length()-4, str.length()).contains("docx")) setIcon(iconPack[15]);
				else if(str.substring(str.length()-4, str.length()).contains("pdf")) setIcon(iconPack[10]);
				else if(str.substring(str.length()-4, str.length()).contains("ppt")) setIcon(iconPack[12]);
				else if(str.substring(str.length()-4, str.length()).contains("pptx")) setIcon(iconPack[12]);
				else if(str.substring(str.length()-4, str.length()).contains("xml")) setIcon(iconPack[18]);
				else if(str.substring(str.length()-4, str.length()).contains("xlsx")) setIcon(iconPack[4]);
				else if(str.substring(str.length()-4, str.length()).contains("gul")) setIcon(iconPack[20]);
				else if(str.substring(str.length()-4, str.length()).contains("html")) setIcon(iconPack[22]);
				// 압축파일
				else if(str.substring(str.length()-4, str.length()).contains("zip")) setIcon(iconPack[16]);
				else if(str.substring(str.length()-4, str.length()).contains("rar")) setIcon(iconPack[16]);
				else if(str.substring(str.length()-4, str.length()).contains("alz")) setIcon(iconPack[16]);
				else if(str.substring(str.length()-4, str.length()).contains("egg")) setIcon(iconPack[16]);
				else if(str.substring(str.length()-4, str.length()).contains("7z")) setIcon(iconPack[16]);
				// 실행파일
				else if(str.substring(str.length()-4, str.length()).contains("exe")) setIcon(iconPack[5]);
				else if(str.substring(str.length()-4, str.length()).contains("jar")) setIcon(iconPack[19]);
			}
		} 
		return this;
	}
}