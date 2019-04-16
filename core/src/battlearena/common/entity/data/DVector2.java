package battlearena.common.entity.data;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import battlearena.common.entity.Entity;

public class DVector2 extends Data
{

	public final Vector2 Value;

	public DVector2(String Name, Entity Parent, boolean Serialized)
	{
		super(Name, Parent, Serialized);

		Value = new Vector2();
	}

	@Override
	public void send(DataOutput output)
	{
		try
		{
			output.writeFloat(Value.x);
			output.writeFloat(Value.y);
		} catch (IOException e)
		{
			System.err.println("Could not serialize Vector2f value " + GetName());
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInput input)
	{
		try
		{
			Value.set(input.readFloat(), input.readFloat());
		} catch (IOException e)
		{
			System.err.println("Could not deserialize Vector2f value " + GetName());
			e.printStackTrace();
		}
	}

}
