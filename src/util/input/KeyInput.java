package util.input;

import org.lwjgl.input.Keyboard;

public class KeyInput {
	
	public static final int NUM_KEYS = 48;
	
	public static boolean[] keyState = new boolean[NUM_KEYS];
	public static boolean[] prevKeyState = new boolean[NUM_KEYS];
	
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;
	public static final int E = 4;
	public static final int F = 5;
	public static final int G = 6;
	public static final int H = 7;
	public static final int I = 8;
	public static final int J = 9;
	public static final int K = 10;
	public static final int L = 11;
	public static final int M = 12;
	public static final int N = 13;
	public static final int O = 14;
	public static final int P = 15;
	public static final int Q = 16;
	public static final int R = 17;
	public static final int S = 18;
	public static final int T = 19;
	public static final int U = 20;
	public static final int V = 21;
	public static final int W = 22;
	public static final int X = 23;
	public static final int Y = 24;
	public static final int Z = 25;
	public static final int KEY_1 = 26;
	public static final int KEY_2 = 27;
	public static final int KEY_3 = 28;
	public static final int KEY_4 = 29;
	public static final int KEY_5 = 30;
	public static final int KEY_6 = 31;
	public static final int KEY_7 = 32;
	public static final int KEY_8 = 33;
	public static final int KEY_9 = 34;
	public static final int KEY_0 = 35;
	public static final int BACKSPACE = 36;
	public static final int ESC = 37;
	public static final int UP = 38;
	public static final int DOWN = 39;
	public static final int LEFT = 40;
	public static final int RIGHT = 41;
	public static final int SPACE = 42;
	public static final int ENTER = 43;
	public static final int LSHIFT = 44;
	public static final int RSHIFT = 45;
	public static final int LCONTROL = 46;
	public static final int RCONTROL = 47;
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
		keyState[A]         = Keyboard.isKeyDown(Keyboard.KEY_A);
		keyState[B]         = Keyboard.isKeyDown(Keyboard.KEY_B);
		keyState[C]         = Keyboard.isKeyDown(Keyboard.KEY_C);
		keyState[D]         = Keyboard.isKeyDown(Keyboard.KEY_D);
		keyState[E]         = Keyboard.isKeyDown(Keyboard.KEY_E);
		keyState[F]         = Keyboard.isKeyDown(Keyboard.KEY_F);
		keyState[G]         = Keyboard.isKeyDown(Keyboard.KEY_G);
		keyState[H]         = Keyboard.isKeyDown(Keyboard.KEY_H);
		keyState[I]         = Keyboard.isKeyDown(Keyboard.KEY_I);
		keyState[J]         = Keyboard.isKeyDown(Keyboard.KEY_J);
		keyState[K]         = Keyboard.isKeyDown(Keyboard.KEY_K);
		keyState[L]         = Keyboard.isKeyDown(Keyboard.KEY_L);
		keyState[M]         = Keyboard.isKeyDown(Keyboard.KEY_M);
		keyState[N]         = Keyboard.isKeyDown(Keyboard.KEY_N);
		keyState[O]         = Keyboard.isKeyDown(Keyboard.KEY_O);
		keyState[P]         = Keyboard.isKeyDown(Keyboard.KEY_P);
		keyState[Q]         = Keyboard.isKeyDown(Keyboard.KEY_Q);
		keyState[R]         = Keyboard.isKeyDown(Keyboard.KEY_R);
		keyState[S]         = Keyboard.isKeyDown(Keyboard.KEY_S);
		keyState[T]         = Keyboard.isKeyDown(Keyboard.KEY_T);
		keyState[U]         = Keyboard.isKeyDown(Keyboard.KEY_U);
		keyState[V]         = Keyboard.isKeyDown(Keyboard.KEY_V);
		keyState[W]         = Keyboard.isKeyDown(Keyboard.KEY_W);
		keyState[X]         = Keyboard.isKeyDown(Keyboard.KEY_X);
		keyState[Y]         = Keyboard.isKeyDown(Keyboard.KEY_Y);
		keyState[Z]         = Keyboard.isKeyDown(Keyboard.KEY_Z);
		keyState[KEY_1]     = Keyboard.isKeyDown(Keyboard.KEY_1);
		keyState[KEY_2]     = Keyboard.isKeyDown(Keyboard.KEY_2);
		keyState[KEY_3]     = Keyboard.isKeyDown(Keyboard.KEY_3);
		keyState[KEY_4]     = Keyboard.isKeyDown(Keyboard.KEY_4);
		keyState[KEY_5]     = Keyboard.isKeyDown(Keyboard.KEY_5);
		keyState[KEY_6]     = Keyboard.isKeyDown(Keyboard.KEY_6);
		keyState[KEY_7]     = Keyboard.isKeyDown(Keyboard.KEY_7);
		keyState[KEY_8]     = Keyboard.isKeyDown(Keyboard.KEY_8);
		keyState[KEY_9]     = Keyboard.isKeyDown(Keyboard.KEY_9);
		keyState[KEY_0]     = Keyboard.isKeyDown(Keyboard.KEY_0);
		keyState[BACKSPACE] = Keyboard.isKeyDown(Keyboard.KEY_BACK);
		keyState[ESC]       = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
		keyState[UP]        = Keyboard.isKeyDown(Keyboard.KEY_UP);
		keyState[DOWN]      = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		keyState[LEFT]      = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		keyState[RIGHT]     = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		keyState[SPACE]     = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		keyState[ENTER]     = Keyboard.isKeyDown(Keyboard.KEY_RETURN);
		keyState[LSHIFT]    = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		keyState[RSHIFT]    = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		keyState[LCONTROL]  = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		keyState[RCONTROL]  = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static boolean isDown(int key) {
		return keyState[key];
	}
	
	public static boolean isPressed(int key) {
		return keyState[key] && !prevKeyState[key];
	}
}