package algorithm.maximumSubsequence;

import java.util.HashMap;
import java.util.Map;

public class MaxSubSumOptimal {

	public static int maxSubSum(int[] a) {
		int maxSum = 0;
		return maxSum;
	}

	class Solution {
		public int[] twoSum(int[] nums, int target) {
			Map<Integer, Integer> map = new HashMap<>();
			for(int i= 0; i<nums.length; i++) {
				int compare = target - nums[i];
				if(map.containsKey(compare)) {
					return new int[]{map.get(compare), i};
				}
				map.put(nums[i] , i);
			}

			throw new RuntimeException();
		}
	}

	public class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
	class Solution1 {
		public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
			ListNode curListNode = new ListNode(0);
			ListNode p1 = l1, p2 = l2, cur = curListNode;
			int carry = 0;
			while (p1 != null || p2 != null) {
				int x = (p1 == null) ? 0 : p1.val;
				int y = (p2 == null) ? 0 : p2.val;
				int sum = x + y + carry;
				carry = sum / 10;
				cur.next = new ListNode(sum % 10);
				cur = cur.next;
				if(p1 != null)
					p1 = p1.next;
				if(p2 != null)
					p2 = p2.next;
			}

			if(carry > 0) {
				cur.next = new ListNode(carry);
			}

			return curListNode.next;
		}
	}

	static class Solution3 {
		public int lengthOfLongestSubstring(String s) {
			char[] chars = s.toCharArray();
			int j = 0;
			int i = 0;
			int z = 0;
			Map<Character, Integer> map = new HashMap<>();
			for (Character character : chars) {
				j++;
				if (map.containsKey(character)) {
					//发生重复字符出现将i调整为出现重复字符的位置
					i = map.get(character) > i ? map.get(character) : i;
				}
				//当前位置与上一次出现重复字符的位置相减为上一次出现重复字符到目前位置最大距离
				//该距离与之前的最大间隔对比为目前最大间隔
				z = z > j - i ? z : j - i;
				map.put(character, j);
			}

			return z;
		}
	}

	public static void main(String[] args) {
		Solution3 solution3 = new Solution3();
		System.out.println(solution3.lengthOfLongestSubstring("abcaf"));
	}
}
