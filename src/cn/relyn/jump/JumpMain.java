package cn.relyn.jump;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class JumpMain {
	
    private static Point firstPoint;
    private static Point secondPoint;

	public static void main(String[] args) {
		int lastDistance = 0;
		while(true) {
			String command = "cmd /c C:\\Users\\Relyn\\Desktop\\screenshot.bat";
			try {
				Runtime.getRuntime().exec(command).waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        int distance = getDistance("C:/Users/Relyn/Desktop/screenshot.png");
	        if(distance == lastDistance)
	        	continue;
	        lastDistance = distance;
	        int delayMs = 0; // 延迟毫秒数
	        int adjust = 0; // 调整参数
	        if(distance < 190)
	        	adjust = 50;
	        else if(distance < 270)
	        	adjust = 80;
	        else if(distance < 305)
	        	adjust = 110;
	        else if(distance < 345)
	        	adjust = 125;
	        else if(distance < 395)
	        	adjust = 140;
	        else if(distance < 455)
	        	adjust = 160;
	        else if(distance < 555)
	        	adjust = 185;
	        else if(distance < 605)
	        	adjust = 200;
	        else
	        	adjust = 230;
	        delayMs = distance + adjust;
//	        HttpClientUtil.invokeGet("http://192.168.11.2/jfinal_pi/i2cPwmDelay/" + distance, null, "utf-8", 2000, 2000);
//	        HttpClientUtil.invokeGet("http://192.168.11.2/jfinal_pi/gpioRelay/" + delayMs, null, "utf-8", 2000, 2000);
	        System.out.println("[*] 中心点长度：" + distance + "像素，延迟：" + delayMs + "ms，校准：" + adjust + "ms");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}	
	
	/**
	 * 获取距离
	 */
	public static int getDistance(String imgPath) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //自动模式的魔数也改为自动计算
        firstPoint = StartCenterFinder.findStartCenter(bufferedImage);
        secondPoint = EndCenterFinder.findEndCenter(bufferedImage, firstPoint);
        // System.out.println(firstPoint + " , " + secondPoint);
        int distance = secondPoint == null ? 0 : distance(firstPoint, secondPoint);
        if (secondPoint == null || secondPoint.getX() == 0 || distance < 75 ||
                // true || //放开可改为全部用ColorFilterFinder来做下一个中心点的查找
                Math.abs(secondPoint.getX() - firstPoint.getX()) < 38) {
            secondPoint = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
        } else {
            Point colorfilterCenter = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
            if (Math.abs(secondPoint.getX() - colorfilterCenter.getX()) > 20) {
                secondPoint = colorfilterCenter;
            }
        }
        ColorFilterFinder.updateLastShapeMinMax(bufferedImage, firstPoint, secondPoint);
        distance = distance(firstPoint, secondPoint);
        return distance;
	}
	
	/**
	 * 计算两点之间的距离
	 * @param a
	 * @param b
	 * @return
	 */
    public static int distance(Point a, Point b) {// 求两点距离
        return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
    }
}
