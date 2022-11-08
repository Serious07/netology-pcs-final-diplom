import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o) {
        if (o.getCount() == getCount()) {
            return 0;
        } else if (o.getCount() > getCount()){
            return 1;
        } else if(o.getCount() < getCount()){
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageEntry pageEntry = (PageEntry) o;
        return page == pageEntry.page && count == pageEntry.count && pdfName.equals(pageEntry.pdfName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, page, count);
    }

    @Override
    public String toString() {
        return "PageEntry{pdf="+ getPdfName() +
                ", page=" + getPage() +
                ", count=" + getCount() +
                "}";
    }
}
