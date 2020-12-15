package com.hilifecare.model;

public class HiExercise {

	private String id; // ex) edgca000xx20001

	private String name; // 운동명

	private String bodyPart1; // 신체 부위 (주동근)
	private String bodyPart2; // 신체 부위 (협력근)

	private String tool; // 도구

	private String fitness1; // 주 체력 요인
	private String fitness2; // 보조 체력 요인

	private Boolean placeOutdoor; // 운동 장소 (야외)
	private Boolean placeHome; // 운동 장소 (가정)
	private Boolean placeIndoor; // 운동 장소 (실내)
	private Boolean placeGym; // 운동 장소 (헬스장)

	private Integer difficulty; // 난이도: 1 ~ 5 (최고 5)

	private Boolean isUnilateral; // 편측 운동

	private Integer performedNoMax; // 최대 실시 횟수

	private Integer performedNo10s; // 10초당 실시 횟수
	private Integer performedNo20s; // 20초당 실시 횟수
	private Integer performedNo30s; // 30초당 실시 횟수

	private Integer enduranceSecMax; // 최대 버티기(seconds)

	private Boolean enabled; // false: Disabled, true: Enabled
	private Boolean movExists; // false: Not exist, true: exists

	public HiExercise(String id, String name, String bodyPart1, String bodyPart2, String tool, String fitness1,
			String fitness2, Boolean placeOutdoor, Boolean placeHome, Boolean placeIndoor, Boolean placeGym, int difficulty, Boolean isUnilateral, int performedNoMax,
			int performedNo10s, int performedNo20s, int performedNo30s, int enduranceSecMax, Boolean enabled, Boolean movExists) {
		this.id = id; // ex) edgca000xx20001

		this.name = name; // 운동명

		this.bodyPart1 = bodyPart1; // 신체 부위 (주동근)
		this.bodyPart2 = bodyPart2; // 신체 부위 (협력근)

		this.tool = tool; // 도구

		this.fitness1 = fitness1; // 주 체력 요인
		this.fitness2 = fitness2; // 보조 체력 요인

		this.placeOutdoor = placeOutdoor; // 운동 장소 (야외)
		this.placeHome = placeHome; // 운동 장소 (가정)
		this.placeIndoor = placeIndoor; // 운동 장소 (실내)
		this.placeGym = placeGym; // 운동 장소 (헬스장)

		this.difficulty = difficulty; // 난이도: 1 ~ 5 (최고 5)

		this.isUnilateral = isUnilateral; // 편측 운동

		this.performedNoMax = performedNoMax; // 최대 실시 횟수

		this.performedNo10s = performedNo10s; // 10초당 실시 횟수
		this.performedNo20s = performedNo20s; // 20초당 실시 횟수
		this.performedNo30s = performedNo30s; // 30초당 실시 횟수

		this.enduranceSecMax = enduranceSecMax; // 최대 버티기(seconds)

		this.enabled = enabled; // false: Disabled, true: Enabled
		this.movExists = movExists; // false: Not exist, true: exists
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBodyPart1() {
		return bodyPart1;
	}

	public void setBodyPart1(String bodyPart1) {
		this.bodyPart1 = bodyPart1;
	}

	public String getBodyPart2() {
		return bodyPart2;
	}

	public void setBodyPart2(String bodyPart2) {
		this.bodyPart2 = bodyPart2;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public String getFitness1() {
		return fitness1;
	}

	public void setFitness1(String fitness1) {
		this.fitness1 = fitness1;
	}

	public String getFitness2() {
		return fitness2;
	}

	public void setFitness2(String fitness2) {
		this.fitness2 = fitness2;
	}

	public Boolean getPlaceOutdoor() {
		return placeOutdoor;
	}

	public void setPlaceOutdoor(Boolean placeOutdoor) {
		this.placeOutdoor = placeOutdoor;
	}

	public Boolean getPlaceHome() {
		return placeHome;
	}

	public void setPlaceHome(Boolean placeHome) {
		this.placeHome = placeHome;
	}

	public Boolean getPlaceIndoor() {
		return placeIndoor;
	}

	public void setPlaceIndoor(Boolean placeIndoor) {
		this.placeIndoor = placeIndoor;
	}

	public Boolean getPlaceGym() {
		return placeGym;
	}

	public void setPlaceGym(Boolean placeGym) {
		this.placeGym = placeGym;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public Boolean getUnilateral() {
		return isUnilateral;
	}

	public void setUnilateral(Boolean unilateral) {
		isUnilateral = unilateral;
	}

	public Integer getPerformedNoMax() {
		return performedNoMax;
	}

	public void setPerformedNoMax(Integer performedNoMax) {
		this.performedNoMax = performedNoMax;
	}

	public Integer getPerformedNo10s() {
		return performedNo10s;
	}

	public void setPerformedNo10s(Integer performedNo10s) {
		this.performedNo10s = performedNo10s;
	}

	public Integer getPerformedNo20s() {
		return performedNo20s;
	}

	public void setPerformedNo20s(Integer performedNo20s) {
		this.performedNo20s = performedNo20s;
	}

	public Integer getPerformedNo30s() {
		return performedNo30s;
	}

	public void setPerformedNo30s(Integer performedNo30s) {
		this.performedNo30s = performedNo30s;
	}

	public Integer getEnduranceSecMax() {
		return enduranceSecMax;
	}

	public void setEnduranceSecMax(Integer enduranceSecMax) {
		this.enduranceSecMax = enduranceSecMax;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getMovExists() {
		return movExists;
	}

	public void setMovExists(Boolean movExists) {
		this.movExists = movExists;
	}

}
