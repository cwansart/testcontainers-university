package application;

public class GithubDTO {

  private Long id;

  private String node_id;

  private String name;

  private String full_name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNode_id() {
    return node_id;
  }

  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }
}
