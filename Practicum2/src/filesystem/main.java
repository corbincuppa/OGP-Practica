import filesystem.*;

import java.util.ArrayList;

import static java.lang.IO.print;

void main() {
    File file1 = new File("text", FileType.TXT);
    File file2 = new File("people", FileType.PDF);
    File file3 = new File("racehorse", FileType.JAVA);
    File file4 = new File("apple", FileType.TXT);
    File file5 = new File("happyappy", FileType.TXT);
    Directory dir2 = new Directory(null, "wowzers");
    ArrayList<DiskItem> listOfItems = new ArrayList<>();
    Directory dir = new Directory("yoopie");
    listOfItems.add(file1);
    listOfItems.add(file2);
    listOfItems.add(file3);
    listOfItems.add(file4);
    listOfItems.add(file5);
    listOfItems.add(dir2);
    dir.addList(listOfItems);

    funciton(dir, listOfItems);
}

private void funciton(Directory dir, ArrayList<DiskItem> listOfItems) {
    dir.organiseDiskItems();
    print(Collections.sort(listOfItems));
    print(dir.getDiskItems());
}
