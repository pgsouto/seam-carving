import edu.princeton.cs.algs4.Picture;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static String getMode(String input) {
        input = input.toLowerCase();

        if (input.equals("(h)") || input.equals("h") || input.equals("(h)eight") || input.equals("height")) {
            return "height";
        }

        if (input.equals("(w)") || input.equals("w") || input.equals("(w)idth") || input.equals("width")) {
            return "width";
        }

        throw new IllegalArgumentException("Argumento inválido, escreva apenas height ou width");
    }

    public static void resizeWidth(SeamCarver seamCarver) {
        System.out.println("Insira a nova largura:");
        int input = Integer.parseInt(scanner.nextLine());

        Picture pic = seamCarver.resizeDimension("width", input);
        pic.show();
    }

    public static void resizeHeight(SeamCarver seamCarver) {
        System.out.println("Insira a nova altura:");
        int input = Integer.parseInt(scanner.nextLine());

        Picture pic = seamCarver.resizeDimension("height", input);
        pic.show();
    }

    public static void main(String[] args) {
        System.out.println("Você deseja diminuir a altura ou largura?");
        System.out.println("**Dica**");
        System.out.println("Altura: h");
        System.out.println("Largura: w");
        System.out.println("*-*-*-*-*");
        System.out.println("Insira uma opção:");
        String input = scanner.nextLine();
        String mode = getMode(input);

        System.out.println();
        System.out.println("Insira o caminho da imagem:");
        String imagePath = scanner.nextLine();

        Picture inputImg = new Picture(imagePath);
        SeamCarver seamCarver = new SeamCarver(inputImg);

        if (mode.equals("width")) {
            resizeWidth(seamCarver);
        } else if (mode.equals("height")) {
            resizeHeight(seamCarver);
        }
    }
}
