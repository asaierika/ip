import java.util.Scanner;
import java.util.ArrayList;

/**
 * Rabbit is a short-tempered, annoyed bot that puts in her 30% efforts
 *  to help you solve some simple problems as her part-time jpb.
 *
 * @author Jiang Zhimeng
 */
public class Rabbit {
    private static String greet = "-----------------------------------------------------------------------------\n"
            + "-----------------------------------------------------------------------------\n"
            + "Yo...nice to meet you. This is rabbit...Ughhhhh I hate this job.\n"
            + "You can input stuff that you want me to write on this grandma-aged notebook.\n"
            + "-----------------------------------------------------------------------------\n"
            + "1. Type the type of a task followed by its content and time to add it into the list.\n"
            + "   There are three types: todo, deadline and event.\n"
            + "   - To add todo, type 'todo the content' such as 'todo do homework'.\n"
            + "   - To add deadline, type 'deadline the content /the time' such as 'deadline do homework /9am'.\n"
            + "   - To add event, type 'event the content /the time' such as 'event do homework /9am'.\n"
            + "2. Type 'list' then I'll show all the existing lines to you.\n"
            + "3. Type 'mark + the index of an existing task' to marks it as done. Like 'mark 1'.\n"
            + "4. Type 'unmark + the index of an existing task' to unmark a task.\n"
            + "5. Type 'delete + the index of an existing task' to delete it.\n"
            + "-----------------------------------------------------------------------------\n"
            + "Actually why not just do me a favour? Type 'bye' in the console and free both of us.";

    private static String bye = "Thanks a lot. I'm gonna have some carrot tea later. See you...";
    // initialise the list that stores tasks.
    /** a list that keeps all the tasks */
    private static ArrayList<Task> list = new ArrayList<>();
    private enum TaskType {
        TODO, DEADLINE, EVENT;
    }

    /**
     *  Adds the input lines the user types into
     *  a list with a size no more than 100.
     *
     * @param task the type of the task that is to be added.
     * @param input the content (and the time) of the task the user inputs.
     */
    private static void addToList(TaskType task, String input) throws AddToListException {
        if (list.size() == 100) {
            // throws an exception when there are already 100 lines in
            // the list when the user is trying to input a new line.
            throw new AddToListException(AddToListException.Type.FULL);
        }

        try {
            // initialise the task to be added
            Task added = new Todo("");
            switch (task) {
            case TODO:
                added = new Todo(input.substring(5, input.length()));
                list.add(added);
                break;
            case DEADLINE:
                // the content and time of the task
                String deadline = input.substring(9, input.length());
                // the index of the character in the string before which is the content
                int i = scanContent(deadline);
                added = new Deadline(deadline.substring(0, i - 1), deadline.substring(i + 1, deadline.length()));
                list.add(added);
                break;
            case EVENT:
                // the content and time of the task
                String event = input.substring(6, input.length());
                // the index of the character in the string before which is the content
                int j = scanContent(event);
                added = new Event(event.substring(0, j - 1), event.substring(j + 1, event.length()));
                list.add(added);
                break;
            }
            System.out.println("Okay...noted.\n" + added.getContent() + "...Huh? Hope you can remember it.");
        } catch (StringIndexOutOfBoundsException e) {
            // if the format is wrong, there will be a
            // StringIndexOutOfBoundsException, catch it
            // and throw an AddToListException
            throw new AddToListException(AddToListException.Type.FORMAT);
        }
    }

    /**
     * Prints the list of current tasks
     * when the user inputs "list".
     */
    private static void list() {
        if (list.size() == 0) {
            System.out.println("There is no task in the list.");
        }
        for (int i  = 0; i < list.size(); i++ ) {
            int index = i + 1;
            System.out.println(index + ". " + list.get(i));
        }
    }

    /**
     * Marks the task at index i as done
     *
     * @param input the user's input.
     */
    private static void mark(String input) throws MarkUnmarkException {
        try {
            Integer.parseInt(input.substring(5));
        } catch (NumberFormatException ex) {
            // if input is mark + a non-integer,
            // throws an exception due to incorrect format
            throw new MarkUnmarkException(MarkUnmarkException.Type.MARKFORMAT);
        }

        int i = Integer.parseInt(input.substring(5));

        if (i > list.size() || i <= 0) {
            throw new MarkUnmarkException(MarkUnmarkException.Type.INDEX);
        }

        if (list.get(i - 1).isDone()) {
            throw new MarkUnmarkException(MarkUnmarkException.Type.MARKREPEAT);
        }

        System.out.println("Okay...task: " + list.get(i - 1).getContent() + " is marked as done.");
        list.get(i - 1).markDone();
    }

    /**
     * Unmarks the task at index i as not done
     *
     * @param input the user's input.
     */
    private static void unmark(String input) throws MarkUnmarkException {
        try {
            Integer.parseInt(input.substring(7));
        } catch (NumberFormatException ex) {
            // if input is unmark + a non-integer,
            // throws an exception due to incorrect format
            throw new MarkUnmarkException(MarkUnmarkException.Type.UNMARKFORMAT);
        }

        int i = Integer.parseInt(input.substring(7));
        if (i > list.size() || i <= 0) {
            throw new MarkUnmarkException(MarkUnmarkException.Type.INDEX);
        }

        if (!list.get(i - 1).isDone()) {
            throw new MarkUnmarkException(MarkUnmarkException.Type.UNMARKREPEAT);
        }

        System.out.println("Okay...task: " + list.get(i - 1).getContent() + " is unmarked.");
        list.get(i - 1).unmark();
    }

    /**
     * Returning the first word of the string.
     *
     * @param input the user's input
     * @return the index of the character in the string
     * before which is the first word
     */
    private static int scanFunction(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                return i + 1;
            }
        }
       return input.length();
    }

    /**
     * Returns the index that separates the content
     * from the time of the task when the user creates a task.
     *
     * @param input the string after the task type in the input.
     * @return the index of the character in the string before which.
     * is the content, after which is the time, -1 if the format is wrong.
     */
    private static int scanContent(String input) {
        for (int i = 0; i < input.length() - 1; i++) {
            if (input.charAt(i) == ' ' && input.charAt(i + 1) == '/') {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Deletes a specified task from the list.
     *
     * @param input The input from the user intended to delete a task.
     * @throws DeleteException The task to be deleted is not in the list.
     */
    private static void delete(String input) throws DeleteException{
        try {
            Integer.parseInt(input.substring(7));
        } catch (NumberFormatException ex) {
            // if input is delete + a non-integer,
            // throws an exception due to incorrect format
            throw new DeleteException(DeleteException.Type.FORMAT);
        }

        int i = Integer.parseInt(input.substring(7));
        if (i > list.size() || i <= 0) {
            // the index is not within the bound of the list
            throw new DeleteException(DeleteException.Type.INDEX);
        }

        System.out.println("Okay...task: " + list.get(i - 1).getContent() + " is deleted.");
        list.remove(i - 1);
    }

    /**
     * Main method of Rabbit.
     *
     * @param args The commandline arguments.
     */
    public static void main(String[] args) {
        System.out.println(greet);
        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                System.out.println(bye);
                sc.close();
                break;
            }
            // the function that the input is calling
            String function = input.substring(0, scanFunction(input));
            try {
                switch (function) {
                case "list":
                    list();
                    break;
                case "mark ":
                    mark(input);
                    break;
                case "unmark ":
                    unmark(input);
                    break;
                case "todo ":
                    addToList(TaskType.TODO, input);
                    break;
                case "deadline ":
                    addToList(TaskType.DEADLINE, input);
                    break;
                case "event ":
                    addToList(TaskType.EVENT, input);
                    break;
                case "delete ":
                    delete(input);
                    break;
                default:
                    // the user keyed in an invalid input
                    System.out.println("Ummm...what is that? I don't get it.");
                }
            } catch (MarkUnmarkException e) {
                System.out.println(e.toString());
            } catch (AddToListException e) {
                System.out.println(e.toString());
            } catch (DeleteException e) {
                System.out.println(e.toString());
            }
        }
    }
}
