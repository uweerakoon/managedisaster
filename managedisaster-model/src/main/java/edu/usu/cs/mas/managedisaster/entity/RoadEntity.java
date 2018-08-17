package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.common.RoadKind;
import edu.usu.cs.mas.managedisaster.common.RoadOrientation;

@AutoProperty
@Entity
@Table (name = "ROAD")
public class RoadEntity {

  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "X")
  private Integer x;
  
  @Column(name = "Y")
  private Integer y;
  
  @Column(name = "LENGTH")
  private Integer length;
  
  @Column(name = "WIDTH")
  private Integer width;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "ORIENTATION")
  private RoadOrientation orientation;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "KIND")
  private RoadKind roadKind;
  
  @Column(name = "CARS_PASS_TO_HEAD")
  private Integer carsPassToHead;
  
  @Column(name = "CARS_PASS_TO_TAIL")
  private Integer carsPassToTail;
  
  @Column(name = "HUMANS_PASS_TO_HEAD")
  private Integer humansPassToHead;
  
  @Column(name = "HUMANS_PASS_TO_TAIL")
  private Integer humansPassToTail;
  
  @Column(name = "MEDIAN_STRIP")
  private Boolean medianStrip;
  
  @Column(name = "LINES_TO_HEAD")
  private Integer linesToHead;
  
  @Column(name = "LINES_TO_TAIL")
  private Integer linesToTail;
  
  @Column(name = "WIDTH_FOR_WALKERS")
  private Double widthForWalkers;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }
  
  public RoadKind getRoadKind() {
    return roadKind;
  }

  public RoadOrientation getOrientation() {
    return orientation;
  }

  public void setOrientation(RoadOrientation orientation) {
    this.orientation = orientation;
  }

  public void setRoadKind(RoadKind roadKind) {
    this.roadKind = roadKind;
  }

  public Integer getCarsPassToHead() {
    return carsPassToHead;
  }

  public void setCarsPassToHead(Integer carsPassToHead) {
    this.carsPassToHead = carsPassToHead;
  }

  public Integer getCarsPassToTail() {
    return carsPassToTail;
  }

  public void setCarsPassToTail(Integer carsPassToTail) {
    this.carsPassToTail = carsPassToTail;
  }

  public Integer getHumansPassToHead() {
    return humansPassToHead;
  }

  public void setHumansPassToHead(Integer humansPassToHead) {
    this.humansPassToHead = humansPassToHead;
  }

  public Integer getHumansPassToTail() {
    return humansPassToTail;
  }

  public void setHumansPassToTail(Integer humansPassToTail) {
    this.humansPassToTail = humansPassToTail;
  }

  public Boolean getMedianStrip() {
    return medianStrip;
  }

  public void setMedianStrip(Boolean medianStrip) {
    this.medianStrip = medianStrip;
  }

  public Integer getLinesToHead() {
    return linesToHead;
  }

  public void setLinesToHead(Integer linesToHead) {
    this.linesToHead = linesToHead;
  }

  public Integer getLinesToTail() {
    return linesToTail;
  }

  public void setLinesToTail(Integer linesToTail) {
    this.linesToTail = linesToTail;
  }

  public Double getWidthForWalkers() {
    return widthForWalkers;
  }

  public void setWidthForWalkers(Double widthForWalkers) {
    this.widthForWalkers = widthForWalkers;
  }
  
  public RoadEntity withId(Long id){
    setId(id);
    return this;
  }
  
  public RoadEntity withX(int x){
    setX(x);
    return this;
  }
  
  public RoadEntity withY(int y){
    setY(y);
    return this;
  }
  
  public RoadEntity withLength(Integer length){
    setLength(length);
    return this;
  }
  
  public RoadEntity withWidth(Integer width){
    setWidth(width);
    return this;
  }
  
  public RoadEntity withOrientation(RoadOrientation orientation){
    setOrientation(orientation);
    return this;
  }
  
  public RoadEntity withRoadKind(RoadKind roadKind){
    setRoadKind(roadKind);
    return this;
  }
  
  public RoadEntity withCarsPassToHead(Integer carsPassToHead){
    setCarsPassToHead(carsPassToHead);
    return this;
  }
  
  public RoadEntity withCarsPassToTail(Integer carsPassToTail){
    setCarsPassToTail(carsPassToTail);
    return this;
  }
  
  public RoadEntity withHumansPassToHead(Integer humansPassToHead){
    setHumansPassToHead(humansPassToHead);
    return this;
  }
  
  public RoadEntity withHumansPassToTail(Integer humansPassToTail){
    setHumansPassToTail(humansPassToTail);
    return this;
  }
  
  public RoadEntity withMedianStrip(Boolean medianStrip){
    setMedianStrip(medianStrip);
    return this;
  }
  
  public RoadEntity withLinesToHead(Integer linesToHead){
    setLinesToHead(linesToHead);
    return this;
  }
  
  public RoadEntity withLinesToTail(Integer linesToTail){
    setLinesToTail(linesToTail);
    return this;
  }
  
  public RoadEntity withWidthForWalkers(Double widthForWalkers){
    setWidthForWalkers(widthForWalkers);
    return this;
  }
  
  public boolean contains(MutableInt2D coordinate){
    int h_l = this.orientation == RoadOrientation.HORIZONTAL
        ? this.length : this.width;
    int v_w = this.orientation == RoadOrientation.HORIZONTAL
        ? this.width : this.length;
    
    return ( coordinate.x >= this.x
        && coordinate.y >= this.y
        && coordinate.x < this.x + h_l
        && coordinate.y < this.y + v_w);
  }
  
  @Override
  public RoadEntity clone(){
    return new RoadEntity().withId(this.id).withX(this.x).withY(this.y)
        .withLength(this.length).withWidth(this.width).withOrientation(this.orientation)
        .withCarsPassToHead(this.carsPassToHead).withCarsPassToTail(this.carsPassToTail)
        .withHumansPassToHead(this.humansPassToHead).withHumansPassToTail(this.humansPassToTail)
        .withLinesToHead(this.linesToHead).withLinesToTail(this.linesToTail)
        .withMedianStrip(this.medianStrip).withRoadKind(this.roadKind)
        .withWidthForWalkers(this.widthForWalkers);
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
