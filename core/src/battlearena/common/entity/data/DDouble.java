package battlearena.common.entity.data;

import java.io.IOException;

import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import battlearena.common.entity.Entity;

public class DDouble extends Data
{

	public Double Value;

	public DDouble(String Name, Entity Parent, boolean Serialized)
	{
		super(Name, Parent, Serialized);

		Value = 0.0;
	}

	@Override
	public void send(DataOutput output)
	{
		try
		{
			output.writeDouble(Value);
		} catch (IOException e)
		{
			System.err.println("Could not serialize double value " + GetName());
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInput input)
	{
		try
		{
			Value = input.readDouble();
		} catch (IOException e)
		{
			System.err.println("Could not deserialize double value " + GetName());
			e.printStackTrace();
		}
	}

}
