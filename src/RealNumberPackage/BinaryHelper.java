package RealNumberPackage;

public class BinaryHelper {
    /**
     * Converts an integer into a binary array representing that number in binary.
     */
    public static int[] convertNumberToBinaryArray(int num) {
        //raises pow to higher than the number
        int pow = 0;
        while(Math.pow(2,pow) <= num) {
            pow++;
        }
        //reduce it so that 2^pow < num but can now turn num into binary
        pow--;

        int remainder = num;
        int[] ar = new int[pow + 1];
        for(int i = 0; i < ar.length; i++) {
            int val = 0;
            int curPow = (int)Math.pow(2,pow);
            if(remainder - curPow >= 0) {
                val = 1;
                remainder -= curPow;
            }
            ar[i] = val;
            pow--;
        }

        return ar;
    }
    /**
     * Converts a binary array into an integer
     */
    public static int convertBinaryArrayToNumber(int[] ar) {
        int total = 0;
        int pow = 0;
        for(int i = ar.length - 1; i >= 0; i--) {
            total += ar[i] * (int)(Math.pow(2, pow));
            pow++;
        }
        return total;
    }
    /**
     * Helper method for if the given bit array needs to be a certain size and the current bitAr is smaller than required.
     */
    public static int[] fitBitArrayToSize(int[] bitAr, int size) {
        if(size < bitAr.length) {return null;}
        int[] change = new int[size];
        int dex = 0;
        int dif = size - bitAr.length;
        while(dex < dif) {
            change[dex] = 0;
            dex++;
        }
        while (dex < size) {
        change[dex] = bitAr[dex - dif];
        dex++;
        }
        return change;
    }

    /*
     * Adds two arrays of bits together, overflows 
     */
    public static int[] addBitArrs(int[] bitAr1, int[] bitAr2) {
        if(bitAr1.length != bitAr2.length) {System.out.println("Bit arrays improper size!"); return null;}

        int[] newAr = new int[bitAr1.length];
        int carry = 0;
        for(int i = newAr.length - 1; i >= 0; i--) {
            int sum = bitAr1[i] + bitAr2[i] + carry;
            carry = sum > 1 ? 1 : 0;
            newAr[i] = sum % 2;
        }

        return newAr;
    }

    /*
     * Performs the XOR operation on the two bit arrays piecewise
     */
    public static int[] xorBitArrs(int[] bitAr1, int[] bitAr2) {
        if(bitAr1.length != bitAr2.length) {System.out.println("Bit arrays improper size!"); return null;}

        int[] newAr = new int[bitAr1.length];
        for(int i = 0; i < bitAr1.length; i++) {
            newAr[i] = (bitAr1[i] == 1) ^ (bitAr2[i] == 1) ? 1 : 0;
        }

        return newAr;
    }

    public static void main(String[] args) {
        int[] b1 = {1, 0, 1};
        int[] b2 = {1, 1, 1};

        int[] xor = xorBitArrs(b1, b2);

        printAr(xor);
    }

    private static void printAr(int[] ar) {
        for(int i = 0; i < ar.length; i++)
            System.out.print(ar[i] + " ");
    }
}
 