package com.dagu.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.awt.Graphics2D;

public class VerificationUtil {
	
	private int width = 70;
	private int height = 35;
	
	private Random random = new Random();
		
	private String FontName[] = {"宋体","仿宋","华文楷体","黑体","微软雅黑","隶书"};
	private String codes = "23456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Color background = new Color(255, 255, 255);
	private String text;

	public VerificationUtil(){}
	
	private Color randomColor() {
		
		int red = random.nextInt(150);
		int green = random.nextInt(150);
		int blue = random.nextInt(150);
		
		return new Color(red, green, blue);
	}
	
	private Font randomFont() {
		
		int index = random.nextInt(FontName.length);
		int style = random.nextInt(4);
		int size = random.nextInt(5)+24;
		
		return new Font(FontName[index], style, size);
	}
	
	private void drawLine(BufferedImage image){
		
		int num = 3;
		Graphics2D image_g = (Graphics2D) image.getGraphics();
		
		for(int i=0; i<num; i++)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			
			image_g.setStroke(new BasicStroke(1.5F));
			image_g.setColor(randomColor());
			image_g.drawLine(x1, y1, x2, y2);
		}
	}
	
	private char randomchar(){
		
		int index = random.nextInt(codes.length());
		
		return codes.charAt(index);
	}
	
	public BufferedImage getImage() {
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D image_g = (Graphics2D) image.getGraphics();
		
		StringBuilder sb = new StringBuilder();
		
		image_g.setColor(background);
		image_g.fillRect(0, 0, width, height);
		
		for(int i=0; i<4; i++)
		{
			String s = randomchar() +"";
			sb.append(s);
			float x = i*1.0F*width/4;
			image_g.setColor(randomColor());
			image_g.setFont(randomFont());
			image_g.drawString(s, x, height-5);
		}
		
		drawLine(image);
		this.text = sb.toString();
		
		return image;
	}
	
	public String getText() {
		
		return this.text;		
	}
	
	public static void outPut(BufferedImage image, OutputStream out) throws IOException {
		
		ImageIO.write(image, "JPEG", out);
	}
 }
