package iris;

public class Test {
    public static int [] indices = new int[120];

    public static void main(String[] args) {

//        for (int k = 0; k < 5; k++) {
//            switch (k) {
//                case 0:
//                    ind(120);
//                    break;
//                case 1:
//                    ind(150);
//                case 2:
//                    ind(30);
//                case 3:
//                    ind(60);
//                case 4:
//                    ind(90);
//            }
            int start = 0;
            for (int i = 0; i < 120 ; i++) {
                indices[i] = start+i;
                if(indices[i] >= 150){
                    indices[i] -= 150;
                }

//                if (i >= stop) {
//                    indices[i] = i + 30;
//                    if (indices[i] > 150){
//                        indices[i] -= (150);
//                    }
//                }
//                else indices[i] = stop+i;
            }

            for (int i = 0; i < indices.length ; i++) {
                System.out.println(indices[i]);
            }

        }
//    }

    public static void ind(int stop){
//        int s = stop;

    }
}
