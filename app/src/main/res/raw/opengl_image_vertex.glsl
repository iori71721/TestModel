precision mediump float;
attribute vec4 a_position;
attribute vec2 a_textureCoordinate;
varying vec2 v_textureCoordinate;
void main() {
    float temp=a_textureCoordinate.x;
    if(temp==0.0){
        temp=1.0;
    }else{
        //test if else if
        if(temp==1.0){
            temp=0.0;
        }
    }
    v_textureCoordinate=a_textureCoordinate;
    gl_Position = a_position;
}