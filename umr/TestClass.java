/*** Test class*/import java.util.*;class TestClass {    private int rec(int r){

        if (true){
            if (false){

            }
        }

        for (int i = 0; i <100 ; i++)
            for (int j = 0; j <100 ; j++) {
                for (int k = 0; k <4 ; k++) {
                    System.out.println();
                }
            }


        for (int i = 0; i <10 ; i++) {
            System.out.println();
            for (int j = 0; j < 10; j++) {
                System.out.println();
                for (int k = 0; k < 15; k++) {
                    System.out.println();
                    for (int l = 0; l < 111; l++) {
                        System.out.println();
                        return 1;
                    }
                }
            }
        }
        if (r == 1) return 1;
        else return rec(r /2);
    }}