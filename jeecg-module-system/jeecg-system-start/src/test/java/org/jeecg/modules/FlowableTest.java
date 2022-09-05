package org.jeecg.modules;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.flowable.common.enums.FlowComment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张少林
 * @date 2022年09月04日 2:03 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class FlowableTest {

    @Resource
    protected TaskService taskService;

    @Test
    public void todoList() {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateOrAssigned("admin").orderByTaskCreateTime().desc();
        List<Task> list = taskQuery.list();
        System.out.println(list);
    }

    @Test
    public void testAddCandidateUser() {
        //任务列表
        List<Task> task2List = taskService.createTaskQuery().processInstanceId("1c4f0fd6-2c07-11ed-a5ea-f6ca1f2bb260").active().list();

//        for (Task task2One : task2List) {
//            taskService.addCandidateUser(task2One.getId(), "admin");
//        }


    }

    @Test
    public void testDeleteCandidateUser() {
        //任务列表
        List<Task> task2List = taskService.createTaskQuery().processInstanceId("1c4f0fd6-2c07-11ed-a5ea-f6ca1f2bb260").active().list();
        for (Task task2One : task2List) {
            taskService.deleteCandidateUser(task2One.getId(), "admin");
        }
    }

    @Test
    public void testComplete() {
        String taskId = "34c6ee76-2c58-11ed-9ba2-5e6c283f4e7f";
        taskService.addComment(taskId, "1c4f0fd6-2c07-11ed-a5ea-f6ca1f2bb260", FlowComment.NORMAL.getType(), "审批通过");
        taskService.setAssignee(taskId, "admin");
        taskService.complete(taskId, null);
    }
}
