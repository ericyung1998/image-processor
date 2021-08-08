import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ImageProcessingApplication
{
	public static void main(String[] args)
	{
		String str = "Introduction to Computing with Java";
		ImageFrame iFrameOriginal = new ImageFrame(str);
		iFrameOriginal.showWin();
		iFrameOriginal.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
}