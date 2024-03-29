package cyou.equinox;

import org.json.JSONObject;

public class Block extends Sprite {

    private EBlockType blockType;

    public Block(EBlockType type, Vector2 position) {
        super(position, new Vector2(32, 32));

        this.blockType = type;
        assignSpriteImage(type);
    }

    public Block(Vector2 position) {
        super(position, new Vector2(32, 32));

        this.blockType = EBlockType.PLACEHOLDER;
    }

    public EBlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(EBlockType blockType) {
        this.blockType = blockType;
        assignSpriteImage(blockType);
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject objectToReturn = new JSONObject();

        objectToReturn.put("blockType", blockType.name());
        objectToReturn.put("position", position.toJsonObject());

        return objectToReturn;
    }

    private void assignSpriteImage(EBlockType type) {
        String imgPath = "";
        switch (type) {
            case BRICK:
                imgPath = "src/resources/sprites/world/brick.png";
                break;
            case BEDROCK:
                imgPath = "src/resources/sprites/world/bedrock.png";
                break;
            case QUESTION:
                imgPath = "src/resources/sprites/world/question.png";
                break;
            case CONCRETE:
                imgPath = "src/resources/sprites/world/concrete.png";
                break;
        }

        setSpriteImage(imgPath);
    }
}
