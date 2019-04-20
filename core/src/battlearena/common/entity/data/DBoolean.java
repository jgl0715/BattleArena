package battlearena.common.entity.data;

import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import java.io.IOException;

import battlearena.common.entity.Entity;

public class DBoolean extends Data
{

    public Boolean Value;

    public DBoolean(String Name, Entity Parent, boolean Serialized)
    {
        super(Name, Parent, Serialized);

        Value = false;
    }

    @Override
    public void send(DataOutput output)
    {
        try
        {
            output.writeBoolean(Value);
        } catch (IOException e)
        {
            System.err.println("Could not serialize boolean value " + GetName());
            e.printStackTrace();
        }
    }

    @Override
    public void read(DataInput input)
    {
        try
        {
            Value = input.readBoolean();
        } catch (IOException e)
        {
            System.err.println("Could not serialize boolean value " + GetName());
            e.printStackTrace();
        }
    }


}
