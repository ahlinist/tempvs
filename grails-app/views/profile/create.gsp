<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - Create profile</title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-10">
          <tempvs:ajaxForm action="create">
            <tempvs:formField type="text" name="firstName" label="clubProfile.firstName.label" />
            <tempvs:formField type="text" name="lastName" label="clubProfile.lastName.label" />
            <tempvs:formField type="text" name="nickName" label="clubProfile.nickName.label" />
            <tempvs:formField type="text" name="clubName" label="clubProfile.clubName.label" />
            <tempvs:ajaxSubmitButton value="clubProfile.create.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
