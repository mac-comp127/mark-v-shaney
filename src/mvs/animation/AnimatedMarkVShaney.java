package mvs.animation;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.GraphicsText;
import mvs.MarkVShaney;

public class AnimatedMarkVShaney {
    private static final int
        WINDOW_WIDTH = 800,
        WINDOW_HEIGHT = 600;
    private static final double
        MARGIN = 10,
        FONT_SIZE = 20,
        SPACE_WIDTH = 6,
        LINE_SPACING = 30;
    private static final double WORD_RATE = 0.15;

    private Iterator<String> wordGenerator;

    private final CanvasWindow canvas;
    private List<FlyUpAnimation> wordAnimations = new LinkedList<>();
    private double nextWordX, nextWordY;
    private double timeUntilNextWord;
    private Color textColor;

    public AnimatedMarkVShaney() {
        MarkVShaney mvs = new MarkVShaney(2, true);
        mvs.readDefaultCorpus();
        wordGenerator = mvs.generateText().iterator();

        canvas = new CanvasWindow("Mark V. Shaney", WINDOW_WIDTH, WINDOW_HEIGHT);
        canvas.setBackground(Color.BLACK);

        canvas.animate(this::generateWords);
        canvas.animate(this::scroll);
        canvas.animate(dt -> {
            for (FlyUpAnimation animation : wordAnimations) {
                animation.animate(dt);
            }
        });

        nextWordY = WINDOW_HEIGHT / 2;
        newLine();
        randomizeTextColor();
    }

    private void generateWords(double dt) {
        timeUntilNextWord -= dt;
        while (timeUntilNextWord < 0 && wordGenerator.hasNext()) {
            timeUntilNextWord += WORD_RATE;
            showWord(wordGenerator.next());
        }
    }

    private void showWord(String word) {
        if (word.contains("\n")) {
            newLine();
            randomizeTextColor();
            nextWordX += MARGIN * 3;
        }

        GraphicsText wordGraphics = new GraphicsText(word.trim());
        wordGraphics.setFontSize(FONT_SIZE);
        wordGraphics.setFillColor(textColor);
        wordGraphics.setY(canvas.getHeight());
        FlyUpAnimation animation = new FlyUpAnimation(wordGraphics, 0.004 * Math.pow(word.length(), 1.5), 5);
        canvas.add(animation);
        wordAnimations.add(animation);

        if (nextWordX + wordGraphics.getWidth() > canvas.getWidth() - MARGIN) {
            newLine();
        }
        animation.setPosition(nextWordX, nextWordY);
        nextWordX += wordGraphics.getWidth() + SPACE_WIDTH;
    }

    private void newLine() {
        nextWordX = MARGIN;
        nextWordY += LINE_SPACING;
    }

    private void randomizeTextColor() {
        textColor = Color.getHSBColor((float) Math.random(), (float) Math.random(), 1);
    }

    private void scroll(double dt) {
        double progress = (nextWordY + nextWordX / canvas.getWidth() * LINE_SPACING) / canvas.getHeight();
        double dy = -dt * Math.max(0, Math.pow(2, progress - 0.7) - 1) * canvas.getHeight();
        nextWordY += dy;
        for (GraphicsObject word : wordAnimations) {
            word.moveBy(0, dy);
            if (word.getBounds().getMaxY() < 0) {
                canvas.remove(word);
            }
        }
        wordAnimations.removeIf(word -> word.getCanvas() == null);
    }

    public static void main(String[] args) {
        new AnimatedMarkVShaney();
    }
}
