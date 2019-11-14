package mvs;

import java.util.*;

final class WordChoice {
    private static final Random rand = new Random();

    private final List<String> choices = new ArrayList<>();

    public List<String> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    public String getRandomChoice() {
        return choices.get(rand.nextInt(choices.size()));
    }

    public boolean isEmpty() {
        return choices.isEmpty();
    }

    public void addChoice(String choice) {
        choices.add(choice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WordChoice that = (WordChoice) o;
        return Objects.equals(choices, that.choices);
    }

    @Override
    public String toString() {
        return "WordChoice{"
            + "choices=" + choices
            + '}';
    }
}
