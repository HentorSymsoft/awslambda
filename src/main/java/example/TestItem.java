package example;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created 2016-08-26.
 */
@DynamoDBTable(tableName="HenrikTable")
public class TestItem {
    private String id;
    private String name;

    @DynamoDBHashKey(attributeName="Id")
    public String getId() { return id;}
    public void setId(String id) {this.id = id;}

    @DynamoDBAttribute(attributeName="Name")
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return "TestItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}