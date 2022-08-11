package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

open class Renderable(
    private val meshes: MutableList<Mesh>,
    modelMatrix: Matrix4f = Matrix4f(),
    parent: Transformable? = null
): Transformable(modelMatrix, parent),  IRenderable {

    override fun render(shaderProgram: ShaderProgram) {
        for (mesh in meshes) {
            shaderProgram.setUniform("model_matrix", getWorldModelMatrix()) // (??)
            mesh.render(shaderProgram)
        }
    }

}