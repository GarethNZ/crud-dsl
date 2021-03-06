package com.garethnz.cruddsl.octopusdeploy

import com.garethnz.cruddsl.base.ItemApi
import com.garethnz.cruddsl.base.ListAPI
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient

class ProjectList : ListAPI<Array<Project>, Project>() {

    var SpaceId: String? = null
        set(value) {
            field = value
            children.forEach { element ->
                if (element is Project) {
                    element.SpaceId = value
                }
            }
        }
    fun project(init: Project.() -> Unit) = initTag(Project(), init)
    override fun url(): String {
        return url
    }

    companion object {
        const val url = "http://localhost:1322/api/projects/all"
    }

    override fun getJsonAdapter(): JsonAdapter<Array<Project>> {
        val moshi = Moshi.Builder()
            // ... add your own JsonAdapters and factories ...
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi.adapter(Array<Project>::class.java)
    }

    override fun getChildElements(): MutableList<Project> {
        return children.filterIsInstance<Project>().toMutableList()
    }

    override fun listOfChildren(sourceData: Array<Project>): Iterator<Project> {
        return sourceData.iterator()
    }
}

class Project : ItemApi<Project>() {
    fun deploymentprocess(init: DeploymentProcess.() -> Unit) = initTag(DeploymentProcess(ProjectId = Id, SpaceId = SpaceId), init)

    // Id can't be set, so don't bother specifying it. We can also get it from the server when we are running as the unique Id is also "Name"
    // Though if someone changes the Name....
    private var Id: String? = null // If we need to create then we get a new Id?
        set(value) {
            field = value
            children.forEach { element ->
                if (element is DeploymentProcess) {
                    element.ProjectId = value
                }
            }
        }
    var SpaceId: String? = null
        set(value) {
            field = value
            children.forEach { element ->
                if (element is DeploymentProcess) {
                    element.SpaceId = value
                }
            }
        }
    private val VariableSetId: String
            get() = "variableset-$Id"
    // The DeploymentProcessId don't seem to be able to be changed, so project is correct as to the process ID, and there for use that to set the Id of the below
    private val DeploymentProcessId: String
        get() = "deploymentprocess-$Id"
    var DiscreteChannelRelease = false
    var IncludedLibraryVariableSetIdS: String? = null
    var DefaultToSkipIfAlreadyInstalled = false
    var TenantedDeploymentMode = "Untenanted"
    var VersioningStrategy = VersioningStrategy("#{Octopus.Version.LastMajor}.#{Octopus.Version.LastMinor}.#{Octopus.Version.NextPatch}")
    var ReleaseCreationStrategy = ReleaseCreationStrategy(null,null,null) // IS THIS ACTUALLY THE DEFAULT?
    var Templates: List<String?> = arrayListOf()
    var AutoDeployReleaseOverrides: List<String>? = arrayListOf()
    var Name: String? = null
    // TODO: Can't write it... just ignore? var Slug: String? = null
    var Description: String = ""
    var IsDisabled = false
    var ProjectGroupId: String? = null
    var LifecycleId: String? = null
    var AutoCreateRelease = false
    var DefaultGuidedFailureMode = "EnvironmentDefault"
    var ProjectConnectivityPolicy = ProjectConnectivityPolicy("None", arrayListOf())
    var ClonedFromProjectId: String? = null
    var ExtensionSettings: Array<String> = arrayOf()
    var ReleaseNotesTemplate: String? = null

    override fun setPrimaryId(destinationPrimary: Project) {
        Id = destinationPrimary.Id
    }

    override fun primaryKeyEquals(target: Project): Boolean {
        return Name == target.Name
    }

    override fun itemUrl(type: HttpRequestType): String {
        return when(type) {
            HttpRequestType.POST -> url
            HttpRequestType.GET, HttpRequestType.PUT, HttpRequestType.DELETE -> url + Id
        }
    }

    companion object {
        const val url = "http://localhost:1322/api/projects/"
    }

    override fun userVisibleName(): String {
        return Name!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        if (Id != other.Id) return false
        if (VariableSetId != other.VariableSetId) return false
        if (DeploymentProcessId != other.DeploymentProcessId) return false
        if (DiscreteChannelRelease != other.DiscreteChannelRelease) return false
        if (IncludedLibraryVariableSetIdS != other.IncludedLibraryVariableSetIdS) return false
        if (DefaultToSkipIfAlreadyInstalled != other.DefaultToSkipIfAlreadyInstalled) return false
        if (TenantedDeploymentMode != other.TenantedDeploymentMode) return false
        if (VersioningStrategy != other.VersioningStrategy) return false
        if (ReleaseCreationStrategy != other.ReleaseCreationStrategy) return false
        if (Templates != other.Templates) return false
        if (AutoDeployReleaseOverrides != other.AutoDeployReleaseOverrides) return false
        if (Name != other.Name) return false
        //if (Slug != other.Slug) return false
        if (Description != other.Description) return false
        if (IsDisabled != other.IsDisabled) return false
        if (ProjectGroupId != other.ProjectGroupId) return false
        if (LifecycleId != other.LifecycleId) return false
        if (AutoCreateRelease != other.AutoCreateRelease) return false
        if (DefaultGuidedFailureMode != other.DefaultGuidedFailureMode) return false
        if (ProjectConnectivityPolicy != other.ProjectConnectivityPolicy) return false
        if (ClonedFromProjectId != other.ClonedFromProjectId) return false
        if (!ExtensionSettings.contentEquals(other.ExtensionSettings)) return false
        if (ReleaseNotesTemplate != other.ReleaseNotesTemplate) return false
        if (SpaceId != other.SpaceId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Id?.hashCode() ?: 0
        result = 31 * result + VariableSetId.hashCode()
        result = 31 * result + DeploymentProcessId.hashCode()
        result = 31 * result + DiscreteChannelRelease.hashCode()
        result = 31 * result + (IncludedLibraryVariableSetIdS?.hashCode() ?: 0)
        result = 31 * result + DefaultToSkipIfAlreadyInstalled.hashCode()
        result = 31 * result + TenantedDeploymentMode.hashCode()
        result = 31 * result + VersioningStrategy.hashCode()
        result = 31 * result + ReleaseCreationStrategy.hashCode()
        result = 31 * result + Templates.hashCode()
        result = 31 * result + (AutoDeployReleaseOverrides?.hashCode() ?: 0)
        result = 31 * result + (Name?.hashCode() ?: 0)
        //result = 31 * result + (Slug?.hashCode() ?: 0)
        result = 31 * result + Description.hashCode()
        result = 31 * result + IsDisabled.hashCode()
        result = 31 * result + (ProjectGroupId?.hashCode() ?: 0)
        result = 31 * result + (LifecycleId?.hashCode() ?: 0)
        result = 31 * result + AutoCreateRelease.hashCode()
        result = 31 * result + DefaultGuidedFailureMode.hashCode()
        result = 31 * result + ProjectConnectivityPolicy.hashCode()
        result = 31 * result + (ClonedFromProjectId?.hashCode() ?: 0)
        result = 31 * result + ExtensionSettings.contentHashCode()
        result = 31 * result + (ReleaseNotesTemplate?.hashCode() ?: 0)
        result = 31 * result + (SpaceId?.hashCode() ?: 0)
        return result
    }

    override fun readFromServer(client: OkHttpClient) : Project? {
        super.readFromServer(client)?.let {project ->
            deploymentprocess { }.readFromServer(client)?.let { dp ->
                project.children.add(dp)
                return project
            }
        }

        return null
    }
}

data class ProjectConnectivityPolicy (
    val SkipMachineBehavior: String,
    val TargetRoles: List<String?>,
    val AllowDeploymentsToNoTargets: Boolean = false,
    val ExcludeUnhealthyTargets: Boolean = false
)

data class VersioningStrategy (
    val Template: String
)

data class ReleaseCreationStrategy (
    val ChannelId: String?,
    val ReleaseCreationPackage: String?,
    val ReleaseCreationPackageStepId: String?
)

