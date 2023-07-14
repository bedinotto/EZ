package syntacticTree;

public class ListNode extends GeneralNode {
    public GeneralNode node;
    public ListNode next;

    public ListNode(GeneralNode t2) {
        super(t2.position); 
        node = t2;
        next = null;
    }

    public ListNode(GeneralNode t2, ListNode l) {
        super(t2.position);
        node = t2;
        next = l;
    }

    public void add(GeneralNode t2) {
        if (next == null) { 
            next = new ListNode(t2); 
        } else {
            next.add(t2);
        }
    }
}
