package scaper

class Rounding {
    public BigDecimal round(int n) {
        return setScale(n, BigDecimal.ROUND_HALF_UP);
    }
}
