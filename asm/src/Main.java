import java.util.LinkedList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static LinkedList<Student> students = new LinkedList<>(); // Thay ArrayList bằng LinkedList

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("1. Add Student");
                System.out.println("2. Edit Student");
                System.out.println("3. Delete Student");
                System.out.println("4. Sort Students");
                System.out.println("5. Search Student");
                System.out.println("6. Display Students");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int option = sc.nextInt();

                switch (option) {
                    case 1:
                        addStudent(sc);
                        break;
                    case 2:
                        editStudent(sc);
                        break;
                    case 3:
                        deleteStudent(sc);
                        break;
                    case 4:
                        System.out.println("Choose sorting algorithm:");
                        System.out.println("1. Heap Sort (Ascending)");
                        System.out.println("2. Merge Sort (Descending)");
                        int sortChoice = sc.nextInt();
                        if (sortChoice == 1) {
                            heapSort();
                            System.out.println("Students sorted in ascending order by score:");
                        } else if (sortChoice == 2) {
                            mergeSort(0, students.size() - 1);
                            System.out.println("Students sorted in descending order by score:");
                        } else {
                            System.out.println("Invalid sorting option.");
                        }
                        displayStudents();
                        break;
                    case 5:
                        searchStudent(sc);
                        break;
                    case 6:
                        displayStudents();
                        break;
                    case 7:
                        System.out.println("Done");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                sc.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Student not found.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void addStudent(Scanner sc) {
        try {
            System.out.print("Enter ID: ");
            String id = sc.next();
            System.out.print("Enter Name: ");
            String name = sc.next();
            if (name.matches(".*\\d.*")) {
                System.out.println("Name cannot contain numbers.");
                return;
            }
            System.out.print("Enter Points: ");
            double marks = sc.nextDouble();
            if (marks < 0 || marks > 10) {
                System.out.println("Score must be between 0 and 10.");
                return;
            }
            students.add(new Student(id, name, marks)); // Sử dụng add() của LinkedList
        } catch (InputMismatchException e) {
            System.out.println("Error: Points must be a number.");
            sc.nextLine();
        }
    }

    private static void editStudent(Scanner sc) {
        System.out.print("Enter ID to edit: ");
        String id = sc.next();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                try {
                    System.out.print("Enter New Name: ");
                    String name = sc.next();
                    if (name.matches(".*\\d.*")) {
                        System.out.println("Name cannot contain numbers.");
                        return;
                    }
                    System.out.print("Enter New Points: ");
                    double marks = sc.nextDouble();
                    if (marks < 0 || marks > 10) {
                        System.out.println("Score must be between 0 and 10.");
                        return;
                    }
                    students.remove(student); // Sử dụng remove() của LinkedList
                    students.add(new Student(id, name, marks)); // Sử dụng add() của LinkedList
                    System.out.println("Student updated.");
                    return;
                } catch (InputMismatchException e) {
                    System.out.println("Error: Points must be a number.");
                    sc.nextLine();
                }
            }
        }
        System.out.println("No student found with the given ID.");
    }

    private static void deleteStudent(Scanner sc) {
        System.out.print("Enter ID to delete: ");
        String id = sc.next();
        boolean removed = students.removeIf(student -> student.getId().equals(id)); // Sử dụng removeIf() của LinkedList
        if (removed) {
            System.out.println("Student has been deleted.");
        } else {
            System.out.println("No student found with the given ID.");
        }
    }

    private static void heapSort() {
        int n = students.size();

        // Bước 1: Xây dựng Max-Heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
        }

        // Bước 2: Trích xuất từng phần tử từ heap
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(students, 0, i);
            heapify(i, 0);
        }
    }

    private static void heapify(int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && students.get(left).getMarks() > students.get(largest).getMarks()) {
            largest = left;
        }

        if (right < n && students.get(right).getMarks() > students.get(largest).getMarks()) {
            largest = right;
        }

        if (largest != i) {
            Collections.swap(students, i, largest);
            heapify(n, largest);
        }
    }

    private static void mergeSort(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(left, mid);
            mergeSort(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private static void merge(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        LinkedList<Student> L = new LinkedList<>();
        LinkedList<Student> R = new LinkedList<>();

        for (int i = 0; i < n1; i++) {
            L.add(students.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            R.add(students.get(mid + 1 + j));
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L.get(i).getMarks() >= R.get(j).getMarks()) {
                students.set(k, L.get(i));
                i++;
            } else {
                students.set(k, R.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            students.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            students.set(k, R.get(j));
            j++;
            k++;
        }
    }

    private static void searchStudent(Scanner sc) {
        System.out.print("Enter ID to search: ");
        String id = sc.next();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                System.out.println(student);
                return;
            }
        }
        System.out.println("No student found with the given ID.");
    }

    private static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the list.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
}
