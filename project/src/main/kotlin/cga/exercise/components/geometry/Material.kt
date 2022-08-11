package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.opengl.GL13

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var spec: Texture2D,
               var shininess: Float = 60.0f,
               var tcMultiplier : Vector2f = Vector2f(1.0f, 1.0f)){


    fun bind(shaderProgram: ShaderProgram) {
        emit.bind(0)
        shaderProgram.setUniform("emit", 0)

        diff.bind(1) // not needed for now
        shaderProgram.setUniform("diff", 1)

        spec.bind(2) // not needed for now
        shaderProgram.setUniform("spec", 2)

        shaderProgram.setUniform("tcMultiplier", tcMultiplier.x, tcMultiplier.y)
        shaderProgram.setUniform("shininess", shininess)
    }
}