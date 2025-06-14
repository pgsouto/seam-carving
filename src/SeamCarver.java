import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.HashMap;

public class SeamCarver {

    private Picture imagem;
    private int largura;
    private int altura;

    public SeamCarver(Picture imagem) {
        this.imagem = new Picture(imagem);
        largura = imagem.width();
        altura = imagem.height();
    }

    public Picture picture(){
        return imagem;
    }

    public int width(){
        return largura;
    }

    public int height(){
        return altura;
    }

    public double energy(int x, int y){
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException("O pixel inserido é inválido");

        if (x == 0 || x == width()-1 || y == 0 || y == height()-1){
            return 10000.0;
        }
        double deltaX2 = 0.0;
        double deltaY2 = 0.0;

        Color x1 = imagem.get(x - 1, y);
        Color y1 = imagem.get(x, y - 1);
        Color x2 = imagem.get(x + 1, y);
        Color y2 = imagem.get(x, y + 1);

        deltaX2 = Math.pow(x2.getRed() - x1.getRed(), 2) + Math.pow(x2.getGreen() - x1.getGreen(), 2) + Math.pow(x2.getBlue() - x1.getBlue(), 2);
        deltaY2 = Math.pow(y2.getRed() - y1.getRed(), 2) + Math.pow(y2.getGreen() - y1.getGreen(), 2) + Math.pow(y2.getBlue() - y1.getBlue(), 2);

        return deltaX2 + deltaY2;
    }

    private int strToIndex(String type, String strPixel){
        if(type.equals("v")){
            return Integer.parseInt(strPixel.split(" ")[0]);
        } else if (type.equals("h")) {
            return Integer.parseInt(strPixel.split(" ")[1]);
        } else{
            throw new IllegalArgumentException("Tipo inválido: apenas h ou v");
        }
    }

    private String indexToStr( int col, int row){
        return col + " " + row;
    }

    private int[] reconstructSeamPath(String type, HashMap<String, String> edgeTo, String lastPixel){
        int n;
        String currentPixel = lastPixel;

        if (type.equals("h")){
            n = width();
        } else if(type.equals("v")){
            n = height();
        }else{
            throw new IllegalArgumentException("tipo inválido: apenas h ou v");
        }

        int[] path = new int[n];

        while(n > 0){
            --n;
            int index = strToIndex(type, currentPixel);
            path[n] = index;
            currentPixel = (String)edgeTo.get(currentPixel);
        }

        return path;
    }

    public int[] findHorizontalSeam(){
        String type = "h";
        HashMap<String, String> edgeTo = new HashMap<String, String>();
        HashMap<String, Double> energyTo = new HashMap<String, Double>();
        double cost = Double.MAX_VALUE;
        String currentPixel = null;
        String nextPixel = null;
        String lastPixel = null;

        for (int col = 0; col < width() - 1; col++) {
            for (int row = 0; row < height(); row++) {

                currentPixel = indexToStr(col, row);
                if (col == 0){
                    edgeTo.put(currentPixel, null);
                    energyTo.put(currentPixel, energy(col, row));
                }

                for (int i = row - 1; i <= row ; i++) {
                    if(i >= 0 && i < height()) {
                        nextPixel = indexToStr(col + 1, i);
                        double updatedEnergy = energy(col + 1, i) + energyTo.get(currentPixel);

                        if (energyTo.get(nextPixel) == null || updatedEnergy < energyTo.get(nextPixel)) {

                            edgeTo.put(nextPixel, currentPixel);
                            energyTo.put(nextPixel, updatedEnergy);

                            if(col + 1 == width() -1 && updatedEnergy < cost) {
                                cost = updatedEnergy;
                                lastPixel = nextPixel;
                            }
                        }
                    }
                }
            }
        }
        return reconstructSeamPath(type, edgeTo, lastPixel);
    }

    public int[] findVerticalSeam() {
        String type = "v";
        HashMap<String, String> edgeTo = new HashMap<String, String>();
        HashMap<String, Double> energyTo = new HashMap<String, Double>();
        double cost = Double.MAX_VALUE;
        String currentPixel = null;
        String nextPixel = null;
        String lastPixel = null;

        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {

                currentPixel = indexToStr(col, row);
                if (row == 0){
                    edgeTo.put(currentPixel, null);
                    energyTo.put(currentPixel, energy(col, row));

                }
                for (int k = col -1; k <= col + 1 ; k++) {
                    if(k >= 0 && k < width()){
                        nextPixel = indexToStr(k, row + 1);
                        double updatedEnergy = energy(k, row +1) + energyTo.get(currentPixel);

                        if (energyTo.get(nextPixel) == null || updatedEnergy < energyTo.get(nextPixel)) {

                            edgeTo.put(nextPixel, currentPixel);
                            energyTo.put(nextPixel, updatedEnergy);
                            if (row + 1 == height() - 1 && updatedEnergy < cost) {
                                cost = updatedEnergy;
                                lastPixel = nextPixel;
                            }
                        }
                    }
                }
            }
        }
        return reconstructSeamPath(type, edgeTo, lastPixel);
    }

    private boolean validateSeam (int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if(Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam) {
        // O tamnho de um array nunca é negativo, preciso dar uma olhada
        if(width() <= 1 || height() <= 1 || seam.length > width() || !validateSeam(seam)){
            throw new IllegalArgumentException("Seam inválido");
        }

        Picture updatedPicture = new Picture(width(), height() -1);

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {

                if (row < seam[col]){
                    updatedPicture.set(col, row, imagem.get(col, row));
                }else{
                    updatedPicture.set(col, row, imagem.get(col, row +1));
                }
            }
        }
        altura--;
        imagem = new Picture(updatedPicture);
    }

    public void removeVerticalSeam(int[] seam) {
        if(width() <= 1 || height() <= 1 || seam.length > height() || !validateSeam(seam)){
            throw new IllegalArgumentException("Seam inválido");
        }

        Picture updatedPicture = new Picture(width() -1, height());

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width() - 1; col++) {
                if (col < seam[row]){
                    updatedPicture.set(col, row, imagem.get(col, row));
                }else{
                    updatedPicture.set(col, row, imagem.get(col + 1, row));
                }
            }
        }
        largura--;
        imagem = new Picture(updatedPicture);
    }

    public Picture resizeDimension(String type, int dimension){
        if (type.equals("width")) {
            while(this.width() > dimension) {
                System.out.println("Ajustando tamanho. Largura atual: " + this.width());
                int[] seam = this.findVerticalSeam();
                this.removeVerticalSeam(seam);
            }
        } else if (type.equals("height")) {
            while(this.height() > dimension) {
                System.out.println("Ajustando tamanho. Altura atual: " + this.height());
                int[] seam = this.findHorizontalSeam();
                this.removeHorizontalSeam(seam);
            }
        } else {
            throw new IllegalArgumentException("Não foi possível fazer o redimensionamento");
        }

        return this.picture();
    }

}
