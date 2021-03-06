package org.openlmis.referencedata.domain;

import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Products that are Orderable by Program.  An OrderableProduct represent any medical commodities
 * that may be ordered/requisitioned, typically by a {@link Program}.
 */
@Entity
@DiscriminatorColumn(name = "Type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "orderable_products", schema = "referencedata")
@NoArgsConstructor
public abstract class OrderableProduct extends BaseEntity {
  private String productCode;
  private long packSize;

  @ElementCollection
  private Set<String> programProducts;

  protected OrderableProduct(String productCode, long packSize) {
    this.productCode = productCode;
    this.packSize = packSize;
    this.programProducts = new LinkedHashSet<>();
  }

  /**
   * Return this orderable product's unique product code.
   * @return a copy of this product's unique product code.
   */
  public final String getProductCode() {
    return productCode;
  }

  /**
   * Adds product for ordering within a program.
   * @param programProduct the association to a {@link org.openlmis.referencedata.domain.Program}
   * @return true if successful, false otherwise.
   */
  public final boolean addToProgram(ProgramProduct programProduct) {
    return programProducts.add("todo");
  }

  public abstract String getDescription();

  /**
   * Returns the number of packs to order. For this OrderableProduct given a desired number of
   * dispensing units, will return the number of packs that should be ordered.
   * @param dispensingUnits # of dispensing units we'd like to order for
   * @return the number of packs that should be ordered.
   */
  public long packsToOrder(long dispensingUnits) {
    double fractionalPacks = (double) (dispensingUnits) / packSize;
    return 0 >= fractionalPacks ? 0 : (long) Math.ceil(fractionalPacks);
  }

  /**
   * Determines if product may be used to fulfill for the given product.
   * @param product the product we'd like to fulfill for.
   * @return true if this product can fulfill for the given product.  False otherwise.
   */
  public abstract boolean canFulfill(OrderableProduct product);

  /**
   * Determines equality based on product codes.
   * @param object another OrderableProduct, ideally.
   * @return true if the two are semantically equal.  False otherwise.
   */
  @Override
  public final boolean equals(Object object) {
    if (null == object) {
      return false;
    }

    if (!(object instanceof OrderableProduct)) {
      return false;
    }

    return ((OrderableProduct) object).productCode.equalsIgnoreCase(this.productCode);
  }

  @Override
  public final int hashCode() {
    return productCode.hashCode();
  }
}
