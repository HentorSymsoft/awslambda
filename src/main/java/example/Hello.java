package example;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Created 2016-08-24.
 */

//public class Hello {
//    public String handleRequest(String s, Context context) {
//        context.getLogger().log("Input: " + s);
//        return s;
//    }
//}

public class Hello implements RequestHandler<Request, String> {
    private static String tableName = "HenrikTable";

    private static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient();
    private static DynamoDBMapper mapper;
    private static DynamoDB dynamoDB;
    private static Table table;

    static{
        dynamoDBClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
        mapper = new DynamoDBMapper(dynamoDBClient);
        dynamoDB = new DynamoDB(dynamoDBClient);
        table = dynamoDB.getTable(tableName);
    }

    public String handleRequest(Request request, Context context) {
        context.getLogger().log("Input: " + request);

        if (request.isAlt()) {
            return handleAlt(request, context);
        } else {
            switch (request.getOperation()) {
                case "insert":
                    context.getLogger().log("insert");
                    TestItem item = new TestItem();
                    item.setId(request.getPayload().getId());
                    item.setName(request.getPayload().getName());
                    mapper.save(item);
                    break;
                case "update":
                    context.getLogger().log("update");

                    break;
                case "get":
                    context.getLogger().log("get");
                    TestItem item2 = mapper.load(TestItem.class, request.getPayload().getId());
                    return String.valueOf(item2);
                default:
                    context.getLogger().log("Unknown operation");
            }

            return request.getOperation() + ": " + request.getPayload();
        }
    }

    private String handleAlt(Request request, Context context) {
        
        switch (request.getOperation()) {
            case "insert":
                context.getLogger().log("insert");

                Item item = new Item()
                        .withPrimaryKey("Id", request.getPayload().getId())
                        .withString("Name", request.getPayload().getName());
                PutItemOutcome putItemOutcome = table.putItem(item);
                return putItemOutcome.getPutItemResult().toString();
            case "update":
                context.getLogger().log("update");

                break;
            case "get":
                context.getLogger().log("get");
                Item gitem = table.getItem("Id", request.getPayload().getId());
                return gitem.toJSON();
            default:
                context.getLogger().log("Unknown operation");
        }

        return request.getOperation() + ": " + request.getPayload();
    }
}



class Request {
    private String operation;
    private Payload payload;
    private boolean alt;

    public boolean isAlt() {
        return alt;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }

    public Request() {
    }

    public Request(String operation, Payload payload) {
        this.operation = operation;
        this.payload = payload;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Request{" +
                "operation='" + operation + '\'' +
                ", payload=" + payload +
                '}';
    }

}

class Payload {
    String id;
    String name;

    public Payload() {
    }

    public Payload(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


