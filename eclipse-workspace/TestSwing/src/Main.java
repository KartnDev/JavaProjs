
public class Main {

	private static Window w;

	public static void main(String[] args) {
		w = new Window();
		for(int i=0;i<1000;i+=13) {
			w.addElements("Element of: " + i);
		}
		w.UpdateUI();
	}
}
