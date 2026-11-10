package co.bluerebhirt.medicion;

import java.math.BigDecimal;

public record RangoPh(int id, BigDecimal desde, BigDecimal hasta, String estado, String recomendacion) { }
