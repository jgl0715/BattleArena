package battlearena.common.entity.data;

import java.io.IOException;

import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import battlearena.common.entity.Entity;

public class DFloat extends Data
{

	public Float Value;

	public DFloat(String Name, Entity Parent, boolean Serialized)
	{
		super(Name, Parent, Serialized);

		Value = 0.0f;
	}

	@Override
	public void send(DataOutput output)
	{
		try
		{
			output.writeFloat(Value);
		} catch (IOException e)
		{
			System.err.println("Could not serialize float value " + GetName());
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInput input)
	{
		try
		{
			Value = input.readFloat();
		} catch (IOException e)
		{
			System.err.println("Could not serialize float value " + GetName());
			e.printStackTrace();
		}
	}

}
