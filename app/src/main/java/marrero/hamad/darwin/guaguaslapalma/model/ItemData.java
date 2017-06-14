package marrero.hamad.darwin.guaguaslapalma.model;

public class ItemData {

    private final String text;
    private final Integer imageId;

    public ItemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    String getText() {
        return text;
    }

    Integer getImageId() {
        return imageId;
    }
}