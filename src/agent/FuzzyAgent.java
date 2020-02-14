package agent;
import fuzzy.AntecedentThesis;
import fuzzy.InputValue;
import fuzzy.Rule;
import fuzzy.fuzzyinterface.FuzzyInterface1;
import fuzzy.relationmodel.RelationModelFactory;
import fuzzy.set.FuzzySet;
import fuzzy.set.TriangleFuzzySet;
import rocketenv.Rocket;
import rocketenv.RocketListener;
import rocketenv.Space;

import java.util.ArrayList;
import java.util.List;

public class FuzzyAgent extends Agent implements RocketListener {
    private FuzzyInterface1 fuzzyRules;
    private InputValue error;
    private FuzzySet.Range errorRange;

    public FuzzyAgent(Rocket mRocket, Space mSpace) {
        super(mRocket, mSpace);

        RelationModelFactory relation=new RelationModelFactory(RelationModelFactory.MAMDANI_MODEL);

        errorRange=new FuzzySet.Range(-5,5);
        error =new InputValue("エラー",0);
        //前件部のファジィ集合「エラーが・・・」
        FuzzySet isMuchPositive=new TriangleFuzzySet(errorRange,"正方向にとても大きい",2,5,5);
        FuzzySet isWellPositive=new TriangleFuzzySet(errorRange,"正方向にやや大きい",0,3,5);
        FuzzySet isLittlePositive=new TriangleFuzzySet(errorRange,"正方向に少しだけ",0,1,3);
        FuzzySet isLittleNegative=new TriangleFuzzySet(errorRange,"負方向に少しだけ",-3,-1,0);
        FuzzySet isWellNegative=new TriangleFuzzySet(errorRange,"負方向にやや大きい",-5,-3,0);
        FuzzySet isMuchNegative=new TriangleFuzzySet(errorRange,"負方向にとても大きい",-5,-5,2);

        FuzzySet.Range mvRange=new FuzzySet.Range(-5,5);
        List<Double> mvValues=new ArrayList<>();
        for(double mv=mvRange.min;mv<=mvRange.max;mv+=0.5)mvValues.add(mv);
        //後件部のファジィ集合
        FuzzySet downFast=new TriangleFuzzySet(mvRange,"急いで降下する",-5,-5,-3);
        FuzzySet down=new TriangleFuzzySet(mvRange,"降下する",-4,-2,-1);
        FuzzySet downALittle=new TriangleFuzzySet(mvRange,"ゆっくり降下する",-2,-1,0);
        FuzzySet upALittle=new TriangleFuzzySet(mvRange,"ゆっくり上昇する",0,1,2);
        FuzzySet up=new TriangleFuzzySet(mvRange,"上昇する",1,2,4);
        FuzzySet upFast=new TriangleFuzzySet(mvRange,"急いで上昇する",3,5,5);

        fuzzyRules=new FuzzyInterface1(mvValues,relation.getPreferredCombinationModeBetweenRules());
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isMuchPositive),upFast,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isWellPositive),up,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittlePositive),upALittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittleNegative),downALittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isWellNegative),down,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isMuchNegative),downFast,relation.create()));

        mRocket.addCarListener(this);
        onRocketRan(0.1);
    }


    @Override
    public void onRocketRan(double deltaTime) {
        double rowError=mSpace.getSV()-mRocket.getPV();

        //エラー値はerrorの定義域(errorRange)に入っていないといけない←
        if(rowError<errorRange.min)rowError=errorRange.min;
        if(errorRange.max<rowError)rowError=errorRange.max;
        error.setValue(rowError);

        try {
            fuzzyRules.inputValueChanged();
        } catch (Rule.NoAntecedentPartListException e) {
            e.printStackTrace();
        }
        try {
            mRocket.setMV(fuzzyRules.getConsequent());
        } catch (FuzzyInterface1.NoRuleException e) {
            e.printStackTrace();
        } catch (Rule.NoAntecedentPartListException e) {
            e.printStackTrace();
        }
    }
}
