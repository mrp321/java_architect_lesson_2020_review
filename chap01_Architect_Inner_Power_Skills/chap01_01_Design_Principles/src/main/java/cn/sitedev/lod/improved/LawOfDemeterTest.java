package cn.sitedev.lod.improved;

public class LawOfDemeterTest {
    public static void main(String[] args) {
        Employee employee = new Employee();
        TeamLeader teamLeader = new TeamLeader();
        teamLeader.commandCheckNumber(employee);
    }
}
