package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Math

open class TronCamera(
    var fieldOfView: Float = Math.toRadians(90f),
    var aspectRatio: Float = 16f / 9f,
    var nearPlane: Float = 0.1f,
    var farPlane: Float = 100.0f,
    modelMatrix: Matrix4f = Matrix4f(),
    parent: Transformable? = null
):
    Transformable(modelMatrix, parent),
    ICamera
{
    override fun getCalculateViewMatrix(): Matrix4f {
        // .lookat(
        //      eye –> the position of the camera : getWorldPosition()
        //      center –> the point in space to look at : getWorldPosition().sub(getWorldZAxis())
//                                                          sub() => to remove the offset in Z axis between near plane and camera eye
        //      up –> the direction of 'up' : getWorldYAxis()
        // )
        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f{
        // Define properties for the perspective projection => creates appropiate projection matrix
        return Matrix4f().perspective(fieldOfView, aspectRatio, nearPlane, farPlane)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view_matrix", getCalculateViewMatrix())
        shader.setUniform("projection_matrix", getCalculateProjectionMatrix())
    }

}