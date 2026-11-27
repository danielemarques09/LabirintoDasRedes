
public class NetworkChallenge {

    private final String question;
    private final String answer;
    private boolean solved;

    public NetworkChallenge(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.solved = false;
    }

    public String getQuestion() {
        return question;
    }

    public boolean checkAnswer(String response) {

        if (response == null) {
            return false;
        }

        if (answer.equalsIgnoreCase(response.trim())) {
            solved = true;
            return true;
        }

        return false;
    }

    public boolean isSolved() {
        return solved;
    }
}
