package credits;

import java.util.ArrayList;

public class CreditRollElement extends CreditsElement {
	
	private ScrollingElement[] sections;
	private ArrayList<ScrollingElement> onScreen;
	private int currentSection;
	
	public CreditRollElement(ScrollingElement[] sections) {
		this.sections = sections;
		onScreen = new ArrayList<ScrollingElement>();
		onScreen.add(sections[0]);
		sections[0].start();
	}
	
	public void update() {
		for (int i = 0; i < onScreen.size(); i++) {
			ScrollingElement s = onScreen.get(i);
			s.update();
			if (i == onScreen.size() - 1) { // newest element
				if (s.getEndPos() < 580) { // next section
					currentSection++;
					if (currentSection >= sections.length) return;
					else onScreen.add(sections[currentSection]);
				}
			}
			else if (s.finished()) {
				onScreen.remove(s);
			}
		}
	}
	
	public void render() {
		for (ScrollingElement s : onScreen) s.render();
	}
	
	public boolean finished() {
		return sections[sections.length - 1].finished();
	}
}