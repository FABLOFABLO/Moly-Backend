# package-structure.md

## Package Structure
- DDD-lite 구조
> 의미있는 단위의 패키지&클래스
- root
    - src
        - main
            - java
                - moly
                    - backend
                        - domain
                            - feature
                                - domain
                                    - Entity(Class)
                                    - repository
                                        - Repository(interface)
                                - presentation
                                    - Controller(class)
                                    - dto
                                        - response
                                            - FeatureResponse(class)
                                        - request
                                            - FeatureRequest(class)
                                - exception
                                    - NameException
                                - service
                                    - FeatureService
                        - global
