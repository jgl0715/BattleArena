package battlearena.common.entity.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DVector2;
import battlearena.editor.WorldEditor;

public class BEditorHoverable extends Behavior
{

    public BEditorHoverable(String name, Entity parent)
    {
        super(name, parent);

        AddData(DFloat.class, Entity.EDITOR_RADIUS);
        AddData(DVector2.class, Entity.POSITION);
        AddData(DBoolean.class, Entity.HOVERED);
        AddData(DBoolean.class, Entity.PRESSED);

        SetUpdatable(true);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        float editorRad = Get(Float.class, Entity.EDITOR_RADIUS);
        Vector2 pos = Get(Vector2.class, Entity.POSITION);

        int mwx = WorldEditor.I.getMouseWorldX();
        int mwy = WorldEditor.I.getMouseWorldY();

        float dx = mwx - pos.x;
        float dy = mwy - pos.y;

        if(dx*dx+dy*dy<=editorRad*editorRad)
        {
            Set(Entity.HOVERED, Boolean.TRUE);

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Get(Boolean.class, Entity.HOVERED))
                Set(Entity.PRESSED, Boolean.TRUE);
            else
                Set(Entity.PRESSED, Boolean.FALSE);
        }
        else
        {
            Set(Entity.HOVERED, Boolean.FALSE);
            Set(Entity.PRESSED, Boolean.FALSE);
        }


    }
}
