package edu.usu.cs.mas.managedisaster.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.common.CoalitionForestStatus;
import sim.util.Int2D;
import sim.util.MutableInt2D;

@AutoProperty
@Entity
@Table (name = "FOREST")
public class ForestEntity {
  
  private static final GeometryFactory geometryFactory = new GeometryFactory();
  
  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "NAME")
  private String name;
  
//  @Type(type="org.hibernate.spatial.GeometryType")
  @Column(name = "SHAPE")
  private Polygon shape;
  
  @Column(name = "LABEL_X")
  private Integer labelX;
  
  @Column(name = "LABEL_Y")
  private Integer labelY;
  
  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "burningForest")
  private List<FireEntity> fires;
  
  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "forest")
  private List<CoalitionForestEntity> coalitionForests;
  
  @Transient
  private Int2D labelCoordinate;
  
  @Column(name = "MIN_X")
  private Integer minX;
  
  @Column(name = "MIN_Y")
  private Integer minY;
  
  @Column(name = "MAX_X")
  private Integer maxX;
  
  @Column(name = "MAX_Y")
  private Integer maxY;
  
  @Transient
  private double currentSmoke;
  
  @Transient
  private double currentFire;
  
  @Transient
  private double currentWater;
  
  @Transient
  private MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ForestEntity withId(Long id){
    this.setId(id);
    return this;
  }

  public Polygon getShape() {
    return shape;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ForestEntity withName(String name) {
    setName(name);
    return this;
  }
  
  public void setShape(Polygon shape) {
    this.shape = shape;
  }
  
  public ForestEntity withShape(Polygon shape){
    this.setShape(shape);
    return this;
  }
  
  public Integer getLabelX() {
		return labelX;
	}

	public void setLabelX(Integer labelX) {
		this.labelX = labelX;
	}

	public Integer getLabelY() {
		return labelY;
	}

	public void setLabelY(Integer labelY) {
		this.labelY = labelY;
	}

	public List<FireEntity> getFires() {
		return fires;
	}

	public void setFires(List<FireEntity> fires) {
		this.fires = fires;
	}
	
	public void addFire(FireEntity fire) {
		if(this.fires == null) {
			fires = new ArrayList<>();
		}
		fires.add(fire);
	}

	public Int2D getLabelCoordinate() {
		if(labelCoordinate == null) {
			labelCoordinate = new Int2D(labelX != null ? labelX.intValue() : 0, labelY != null ? labelY.intValue() : 0);
		}
		return labelCoordinate;
	}

	public void setLabelCoordinate(Int2D labelCoordinate) {
		this.labelCoordinate = labelCoordinate;
	}

	public boolean contains(int x, int y){
    if(x < getMinX() || y < getMinY() || x > getMaxX() || y > getMaxY()) {
      return false;
    }
    Point point = geometryFactory.createPoint(new Coordinate(x, y));
    boolean isContain = shape.contains(point);
    return isContain;
  }
  
  public Coordinate[] getBoundaries() {
    Coordinate[] boundaryCoordinates = shape.getBoundary().getCoordinates();
    return boundaryCoordinates;
  }
  
  public Integer getMinX() {
    if(minX == null) {
      setMinMaxCoordinate();
    }
    return minX;
  }

  public Integer getMinY() {
    if(minY == null) {
      setMinMaxCoordinate();
    }
    return minY;
  }

  public Integer getMaxX() {
    if(maxX == null) {
      setMinMaxCoordinate();
    }
    return maxX;
  }

  public Integer getMaxY() {
    if(maxY == null) {
      setMinMaxCoordinate();
    }
    return maxY;
  }

	public void setMinMaxCoordinate() {
    Coordinate[] coordinates = getBoundaries();
    for(Coordinate coordinate : coordinates) {
      if(minX == null || minX > (int)coordinate.x) {
        minX = (int) coordinate.x;
      }
      if(maxX == null || maxX < (int)coordinate.x) {
        maxX = (int) coordinate.x;
      }
      if(minY == null || minY > (int)coordinate.y) {
        minY = (int) coordinate.y;
      }
      if(maxY == null || maxY < (int)coordinate.y) {
        maxY = (int) coordinate.y;
      }
    }
  }
  
  public MutableInt2D getRandomCoordinate() {
    int randomX, randomY;
    MutableInt2D randomLocation = null;
    do {
      randomX = random.nextInt((getMaxX() - getMinX()) + 1) + getMinX();
      randomY = random.nextInt((getMaxY() - getMinY()) + 1) + getMinY();
    } while(!contains(randomX, randomY));
    randomLocation = new MutableInt2D(randomX, randomY);
    return randomLocation;
  }
  
  public double getArea() {
  	double area = shape.getArea();
  	return area;
  }

  public double getCurrentSmoke() {
		return currentSmoke;
	}

	public void setCurrentSmoke(double currentSmoke) {
		this.currentSmoke = currentSmoke;
	}

	public double getCurrentFire() {
		return currentFire;
	}

	public void setCurrentFire(double currentFire) {
		this.currentFire = currentFire;
	}

	public double getCurrentWater() {
		return currentWater;
	}

	public void setCurrentWater(double currentWater) {
		this.currentWater = currentWater;
	}

	public List<CoalitionForestEntity> getCoalitionForests() {
    return coalitionForests;
  }
	
	public List<CoalitionForestEntity> getUtilizedCoalitionForests() {
    return coalitionForests
            .stream()
            .filter(cb -> cb.getStatus() == CoalitionForestStatus.UTILIZED)
            .collect(Collectors.toList());
  }

  public void setCoalitionForests(List<CoalitionForestEntity> coalitionForests) {
    this.coalitionForests = coalitionForests;
  }

  public void addCoalitionForests(CoalitionForestEntity coalitionForests) {
    if(this.coalitionForests == null) {
      this.coalitionForests = new ArrayList<>();
    }
    this.coalitionForests.add(coalitionForests);
  }
  
  @Override
  public boolean equals(Object o){
    return Pojomatic.equals(this, o);
  }
  
  @Override
  public int hashCode(){
    return Pojomatic.hashCode(this);
  }
  
  @Override
  public String toString(){
    return Pojomatic.toString(this);
  }
}
