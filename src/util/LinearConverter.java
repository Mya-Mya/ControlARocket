package util;

/**
 * 2つの座標空間を線形に対応付ける。
 */
public class LinearConverter {

    private boolean isXConstant;
    private double constantX;
    private boolean isYConstant;
    private double constantY;

    private double aXtoY;
    private double bXtoY;
    private double aYtoX;
    private double bYtoX;

    public LinearConverter(double x0, double y0, double x1, double y1) {
        setRelation(x0, y0, x1, y1);
    }

    /**
     * X → Y の変換を提供する。
     *
     * @param x X空間での任意の値
     * @return 変換結果
     */
    public double fromXtoY(double x) {
        if (isYConstant) return constantY;
        if (isXConstant) {
            if (x == constantX) {
                throw new IllegalArgumentException("xは定数(=" + constantX + "につきX→Yは不定");
            } else {
                throw new IllegalArgumentException("Xは定数(=" + constantX + "につきそのようなxはXに含まれていない");
            }
        }
        return aXtoY * x + bXtoY;
    }

    /**
     * Y → X の変換を提供する。
     *
     * @param y Y空間での任意の値
     * @return 変換結果
     */
    public double fromYtoX(double y) {
        if (isXConstant) return constantX;
        if (isYConstant) {
            if (y == constantY) {
                throw new IllegalArgumentException("yは定数(=" + constantY + "につきY→Xは不定");
            } else {
                throw new IllegalArgumentException("Yは定数(=" + constantY + ")につきそのようなyはYに含まれていない");
            }
        }
        return aYtoX * y + bYtoX;
    }

    /**
     * x∈X、y∈YようなXとYを対応付ける。
     *
     * @param x0 X空間での任意の値
     * @param y0 Y空間でのx0に対応する値
     * @param x1 X空間での任意の値
     * @param y1 Y空間でのx1に対応する値
     */
    public void setRelation(double x0, double y0, double x1, double y1) {
        if (x0 == x1) {
            isXConstant = true;
            constantX = x0;
        }
        if (y0 == y1) {
            isYConstant = true;
            constantY = y0;
        }
        if (isXConstant && isYConstant) System.err.println("x0=x1, y0=y1はXとYの対応を作るのには不定");
        if (isXConstant || isYConstant) return;
        /*
        R : X → Y のような一次関数R(x)
        y-y0=(y1-y0)/(x1-x0)*(x-x0)
        y=(y1-y0)/(x1-x0)*x -(y1-y0)/(x1-x0)*x0 +y0
         */
        isXConstant = false;
        aXtoY = (y1 - y0) / (x1 - x0);
        bXtoY = -aXtoY * x0 + y0;
        /*
        T : Y → X のような一次関数T(y)
        x-x0=(x1-x0)/(y1-y0)*(y-y0)
        x=(x1-x0)/(y1-y0)*x -(x1-x0)/(y1-y0)*y0 +x0
         */
        isYConstant = false;
        aYtoX = (x1 - x0) / (y1 - y0);
        bYtoX = -aYtoX * y0 + x0;
    }
}
