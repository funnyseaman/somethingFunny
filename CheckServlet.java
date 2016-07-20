
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CheckServlet
 */
@WebServlet("/CheckServlet")
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int WIDTH = 110;
	private static int HEIGHT = 35;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("image/jpeg");
		HttpSession session = request.getSession();
		ServletOutputStream sos = response.getOutputStream();
		//设置浏览器不要缓存此图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		//创建内存图像并获得其图形上下文
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		//产生随机的认证码
		char[] rands = generateCheckCode();
		//产生图像
		drawBackground(g);
		drawRands(g, rands);
		//结束图像的绘制过程，完成图像
		g.dispose();
		//将图像输出到客户端
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		response.setContentLength(buf.length);
		//下面的语句也可以写成：bos.writeTo(sos);
		sos.write(buf);
		bos.close();
		sos.close();
		//将当前验证码存入到Session中
		session.setAttribute("check_code", new String(rands));
	}
	
	//生产一个6字符的验证码
	private char[] generateCheckCode()
	{
		//定义验证码的字符表
		String chars = "012345678901234567890123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] rands = new char[6];
		for(int i=0;i<6;i++)
		{
			int rand = (int)(Math.random()*82);
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}
	
	private void drawBackground(Graphics g)
	{
		//随机颜色背景{浅颜色}
		int colorCode[] = {0x00FF00, 0x00FFFF, 0xFFFF00, 0x70DB93, 0x7093DB, 0x93DB70, 0x32CD32, 0xE9C2A6, 0xDB70DB};
		int color_code = colorCode[(int)(Math.random()*9)];
		g.setColor(new Color(color_code));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//随机产生600个干扰点
		for(int i=0;i<600;i++)
		{
			//随机位置
			int x = (int)(Math.random()*WIDTH);
			int y = (int)(Math.random()*HEIGHT);
			//随机颜色绘制点
			g.setColor(getRandomColor());
			g.drawOval(x, y, 1, 1);
		}
		//随机产生40条干扰线
		for(int i=0;i<40;i++)
		{
			//两个随机位置
			int x1 = (int)(Math.random()*WIDTH);
			int y1 = (int)(Math.random()*HEIGHT);
			int x2 = (int)(Math.random()*WIDTH);
			int y2 = (int)(Math.random()*HEIGHT);
			//随机颜色绘制线
			g.setColor(getRandomColor());
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	private void drawRands(Graphics g, char[] rands)
	{
//		g.setFont(new Font(null, Font.ITALIC|Font.BOLD, 28));
		//在不同高度上输入验证码的每个字符,颜色随机
		for(int i=0;i<6;i++)
		{
			//随机颜色背景{深颜色}
			int colorCode[] = {0xFF0000, 0x000000, 0xFFFFFF, 0x00009C, 0xBC1717, 0x800000, 0xBC1717};
			int color_code = colorCode[(int)(Math.random()*7)];
			g.setColor(new Color(color_code));
			//随机字形和大小
			g.setFont(new Font(null, Font.ITALIC|Font.BOLD, 26+(int)(Math.random()*4)));
			//开始绘制
			g.drawString(""+rands[i], 1+16*i+(int)(Math.random()*1), 22+(int)(Math.random()*8));
		}
	}
	
	private Color getRandomColor()
	{
		int red = (int)(Math.random()*255);
		int green = (int)(Math.random()*255);
		int blue = (int)(Math.random()*255);
		return new Color(red, green, blue);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
