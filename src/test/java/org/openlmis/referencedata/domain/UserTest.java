package org.openlmis.referencedata.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.openlmis.referencedata.exception.RightTypeException;

public class UserTest {
  private RightQuery rightQuery = new RightQuery(Right.ofType(RightType.SUPERVISION));

  private RoleAssignment assignment1 = mock(RoleAssignment.class);

  private RoleAssignment assignment2 = mock(RoleAssignment.class);

  private User user;

  private String roleName = "role";

  @Before
  public void setUp() {
    user = new User();
  }

  @Test
  public void shouldBeAbleToAssignRoleToUser() throws RightTypeException {
    //given


    //when
    user.assignRoles(new DirectRoleAssignment(new Role(roleName, Right.ofType(RightType.REPORTS))));

    //then
    assertThat(user.getRoleAssignments().size(), is(1));
  }

  @Test
  public void shouldHaveRightIfAnyRoleAssignmentHasRight() {
    //given
    user.assignRoles(assignment1);
    user.assignRoles(assignment2);

    when(assignment1.hasRight(rightQuery)).thenReturn(true);
    when(assignment2.hasRight(rightQuery)).thenReturn(false);

    //when
    boolean hasRight = user.hasRight(rightQuery);

    //then
    assertTrue(hasRight);
  }

  @Test
  public void shouldNotHaveRightIfNoRoleAssignmentHasRight() {
    //given
    user.assignRoles(assignment1);
    user.assignRoles(assignment2);

    when(assignment1.hasRight(rightQuery)).thenReturn(false);
    when(assignment2.hasRight(rightQuery)).thenReturn(false);

    //when
    boolean hasRight = user.hasRight(rightQuery);

    //then
    assertFalse(hasRight);
  }
}
