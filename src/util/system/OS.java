package util.system;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Optional;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class OS {
	
	public static int OS;
	
	public static final int WINDOWS = 0;
	public static final int MAC = 1;
	public static final int LINUX = 2;
	public static final int WINDOWS_OLD = 3;
	
	public static void checkOS() {
		String osName = System.getProperty("os.name");
		System.out.println("[OS] OS: " + osName);
		if (osName.indexOf("win") > 0) {
			OS = WINDOWS;
			if (osName.indexOf("10") == -1) OS = WINDOWS_OLD;
		}
		else if (osName.indexOf("mac") > 0) OS = MAC;
		else if (osName.indexOf("nix") > 0 || osName.indexOf("nux") > 0 || osName.indexOf("aix") > 0) OS = LINUX;
	}

	
	public static void processCheck() {
	    ProcessHandle.allProcesses().forEach(process -> System.out.println(processDetails(process)));
	}

	private static String processDetails(ProcessHandle process) {
	    return String.format("%8d %8s %10s %26s %-40s",
	            process.pid(),
	            text(process.parent().map(ProcessHandle::pid)),
	            text(process.info().user()),
	            text(process.info().startInstant()),
	            text(process.info().commandLine()));
	}
	
	private static String text(Optional<?> optional) {
	    return optional.map(Object::toString).orElse("-");
	}
	
	public static void minimizeAll() {
		try {
			HWND hwnd = User32.INSTANCE.FindWindow(null, "Doki Doki Platformer Club!");
			User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MINIMIZE);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_WINDOWS);
			r.keyPress(KeyEvent.VK_D);
			r.keyRelease(KeyEvent.VK_D);
			r.keyRelease(KeyEvent.VK_WINDOWS);
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void restoreWindow() {
		HWND hwnd = User32.INSTANCE.FindWindow(null, "Doki Doki Platformer Club!");
		User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOWNOACTIVATE);
		boolean success = User32.INSTANCE.SetForegroundWindow(hwnd);
		System.out.println("[OS] Bring to front succeded? " + success);
	}
	
	public static void killExplorerTest() {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_WINDOWS);
			r.keyPress(KeyEvent.VK_R);
			r.keyRelease(KeyEvent.VK_R);
			r.keyRelease(KeyEvent.VK_WINDOWS);
			Thread.sleep(1000);
			r.keyPress(KeyEvent.VK_T);
			r.keyRelease(KeyEvent.VK_T);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_A);
			r.keyRelease(KeyEvent.VK_A);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_S);
			r.keyRelease(KeyEvent.VK_S);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_K);
			r.keyRelease(KeyEvent.VK_K);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_K);
			r.keyRelease(KeyEvent.VK_K);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_I);
			r.keyRelease(KeyEvent.VK_I);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_L);
			r.keyRelease(KeyEvent.VK_L);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_L);
			r.keyRelease(KeyEvent.VK_L);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_SPACE);
			r.keyRelease(KeyEvent.VK_SPACE);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_SLASH);
			r.keyRelease(KeyEvent.VK_SLASH);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_F);
			r.keyRelease(KeyEvent.VK_F);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_SPACE);
			r.keyRelease(KeyEvent.VK_SPACE);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_SLASH);
			r.keyRelease(KeyEvent.VK_SLASH);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_I);
			r.keyRelease(KeyEvent.VK_I);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_M);
			r.keyRelease(KeyEvent.VK_M);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_SPACE);
			r.keyRelease(KeyEvent.VK_SPACE);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_E);
			r.keyRelease(KeyEvent.VK_E);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_X);
			r.keyRelease(KeyEvent.VK_X);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_P);
			r.keyRelease(KeyEvent.VK_P);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_L);
			r.keyRelease(KeyEvent.VK_L);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_O);
			r.keyRelease(KeyEvent.VK_O);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_R);
			r.keyRelease(KeyEvent.VK_R);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_E);
			r.keyRelease(KeyEvent.VK_E);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_R);
			r.keyRelease(KeyEvent.VK_R);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_PERIOD);
			r.keyRelease(KeyEvent.VK_PERIOD);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_E);
			r.keyRelease(KeyEvent.VK_E);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_X);
			r.keyRelease(KeyEvent.VK_X);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_E);
			r.keyRelease(KeyEvent.VK_E);
			Thread.sleep(100);
			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void restoreExplorer() {
		try {
			@SuppressWarnings("unused")
			Process p = Runtime.getRuntime().exec("explorer.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}