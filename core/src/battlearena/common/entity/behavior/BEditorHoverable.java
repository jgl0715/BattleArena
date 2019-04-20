package battlearena.common.entity.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DVector2;
import battlearena.editor.WorldEditor;

public class BEditorHoverable extends Behavior
{

    public BEditorHoverable(String name, Entity parent)
    {
        super(name, parent);
        AddData(DVector2.class, Entity.EDITOR_SIZE);
        AddData(DVector2.class, Entity.POSITION);
        AddData(DBoolean.class, Entity.DATA_HOVERED);
        AddData(DBoolean.class, Entity.DATA_PRESSED);

        SetUpdatable(true);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        Vector2 editorSize = Get(Vector2.class, Entity.EDITOR_SIZE);
        Vector2 pos = Get(Vector2.class, Entity.POSITION);

        int mwx = WorldEditor.I.getMouseWorldX();
        int mwy = WorldEditor.I.getMouseWorldY();

        Vector2 bottomLeft = new Vector2(pos).sub(new Vector2(editorSize).scl(0.5f));
        Vector2 topRight = new Vector2(bottomLeft).add(editorSize);

        if(mwx >= bottomLeft.x && mwy >= bottomLeft.y && mwx < topRight.x && mwy < topRight.y)
        {
            Set(Entity.DATA_HOVERED, Boolean.TRUE);
        }
        else
        {
            Set(Entity.DATA_HOVERED, Boolean.FALSE);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Get(Boolean.class, Entity.DATA_HOVERED))
        {
            Set(Entity.DATA_PRESSED, Boolean.TRUE);
        }
        else
        {
            Set(Entity.DATA_PRESSED, Boolean.FALSE);
        }

    }
}
