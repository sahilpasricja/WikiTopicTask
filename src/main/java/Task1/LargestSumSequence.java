package Task1;
//You are given an array of integers (both positive and negative). Find the continuous sequence
//        with largest sum.
//        Example:
//        Input: (2, -8, 3, -2, 4, -10)
//        Output: 5 (ie., {3, -2, 4}

public class LargestSumSequence {
    public Integer continuesSequence1(int[] nums) {
        //Edge cases
        if(nums == null || nums.length == 0)
            return null;

        if(nums.length == 1)
            return nums[0];

        int curSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for(int i:nums){
            curSum = i + curSum;
            maxSum = Math.max(curSum,maxSum);
            curSum = Math.max(0, curSum);
        }
        return maxSum;

    }


    //Driver function
    public static  void main(String[] args){
        LargestSumSequence largestSumSequence = new LargestSumSequence();
        int result  = largestSumSequence.continuesSequence1(new int[]{2, -8, 3, -2, 4, -10});
        System.out.println( "\n" + result);
    }


}
